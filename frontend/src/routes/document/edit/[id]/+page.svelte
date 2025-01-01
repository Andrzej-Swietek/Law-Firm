<script lang="ts">
    import { onMount } from "svelte";
    import { page } from '$app/stores';
    import { toast } from "svelte-sonner";

    import { Button } from "$lib/components/ui/button/index";
    import { Input } from "$lib/components/ui/input/index";
    import { Textarea } from "$lib/components/ui/textarea/index";
    import { Label } from "$lib/components/ui/label";


    import type {Document, DocumentType} from "$lib/interfaces/document.interface";
    import {Content, Item, Select, Trigger, Value} from "$lib/components/ui/select";
    import type {FileInputEvents} from "lucide-svelte/icons/file-input.svelte";

    import {updateDocument} from "$lib/api/document/updateDocument";
    import {downloadFileFromStorage} from "$lib/api/storage/downloadFileFromStorage";
    import {getDocumentById} from "$lib/api/document/getDocumentById";
    import {getAllDocumentTypes} from "$lib/api/document/documentType/getAllDocumentTypes";


    $: documentId = $page.params.id

    let documentData: Partial<Document & { file?: File|null }> = {
        id: -1,
        title: "",
        description: "",
        typeId: 1,
        file: null
    }

    let documentTypes: DocumentType[] = [];
    let selectedDocumentTypeId: number|null = null;

    const handleSubmit = async () => {
        try {
            if (selectedDocumentTypeId)
                documentData.typeId = selectedDocumentTypeId!;

            const response = await updateDocument(documentId, documentData);

            if (response) {
                toast.success("Document successfully updated!");
            } else {
                toast.error("Failed to create document. Please try again.");
            }
        } catch (error) {
            console.error("Error creating document:", error);
            toast.error("Failed to create document. Please try again.");
        }
    };

    const handleFileChange = (event: any) => {
        documentData.file = event.target.files[0];
    };

    const downloadFile = async() => {
        if (documentData?.filePath)
            await downloadFileFromStorage(documentData?.filePath)
    }

    onMount( async ()=>{
        const data = await getDocumentById(documentId);
        const response = await getAllDocumentTypes();
        documentTypes = [...response];

        documentData = {
            ...data?.document
        }
        selectedDocumentTypeId = data?.document.typeId ?? 1;
    })

</script>

<div class="max-w-4xl mx-auto p-8">
    <h1 class="text-2xl font-bold mb-16">Update Document</h1>

    <form on:submit|preventDefault={handleSubmit} class="space-y-6">
        <!-- Title -->
        <div>
            <Label for="title">Title</Label>
            <Input
                    id="title"
                    placeholder="Enter document title"
                    bind:value={documentData.title}
            />
        </div>

        <!-- Description -->
        <div>
            <Label for="description">Description</Label>
            <Textarea
                    id="description"
                    placeholder="Enter document description"
                    bind:value={documentData.description}
            />
        </div>

        <!-- File Upload -->
        <div>
            <Label for="file">Upload File: <span class="mx-4">{documentData.file ? documentData.file.name : 'No file chosen'}</span> </Label>
            <div class="w-full py-2">
                <Label> Currently Saved: <span class="mx-8">{ documentData?.filePath } </span> </Label>
            </div>
            <Input
                    id="file"
                    on:change={handleFileChange}
                    accept=".pdf,.docx,.txt"
                    type="file"
            />

            <div class="my-4">
                <Button type="button" on:click={() => downloadFile()}>
                    Download Current Document
                </Button>
            </div>

        </div>

        <div>
            <Label for="trial">Document Type</Label>
            <div class="w-full py-2">
                <Label> Currently Saved: <span class="mx-8">{ documentData?.documentType?.name } </span> </Label>
            </div>
            <Select
                    selected={documentTypes.find(docType => docType.id === selectedDocumentTypeId)}
                    onSelectedChange={(selected) => {
                        selected && (selectedDocumentTypeId = Number(selected.value));
                    }}
            >
                <Trigger>
                    <Value placeholder={`Select Document Type ... | Current: ${documentData.documentType?.name ?? ''}`} />
                </Trigger>
                <Content>
                    {#each documentTypes as docType}
                        <Item value={docType.id} label={`${docType.id} - ${docType.name}`} />
                    {/each}
                </Content>
            </Select>
        </div>

        <!-- Submit Button -->
        <Button type="submit" class="w-full mt-6">
            Update Document
        </Button>
    </form>
</div>
