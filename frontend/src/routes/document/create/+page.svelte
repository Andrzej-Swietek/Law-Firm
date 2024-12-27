<script lang="ts">
    import { onMount } from "svelte";
    import { toast } from "svelte-sonner";

    import { Button } from "$lib/components/ui/button/index";
    import { Input } from "$lib/components/ui/input/index";
    import { Textarea } from "$lib/components/ui/textarea/index";
    import { Label } from "$lib/components/ui/label";


    import { createDocument } from "$lib/api/document/createDocument";
    import type {Document, DocumentType} from "$lib/interfaces/document.interface";
    import {getAllDocumentTypes} from "$lib/api/document/documentType/getAllDocumentTypes";
    import {Content, Item, Select, Trigger, Value} from "$lib/components/ui/select";
    import type {FileInputEvents} from "lucide-svelte/icons/file-input.svelte";


    let documentData: Partial<Document & { file?: File|null }> = {
        title: "",
        description: "",
        typeId: 1,
        file: null
    }

    let documentTypes: DocumentType[] = [];
    let selectedDocumentTypeId: number|null = null;

    const handleSubmit = async () => {
         if (!documentData?.title?.trim() || !selectedDocumentTypeId || !documentData?.description?.trim() || !documentData?.file) {
            toast.error("Please fill out all required fields and upload a file.");
            return;
        }

        try {
            documentData.typeId = selectedDocumentTypeId!;
            console.log(documentData)
            const response = await createDocument(documentData);

            if (response) {
                toast.success("Document successfully created!");
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

    onMount( async ()=>{
        const response = await getAllDocumentTypes();
        documentTypes = [...response]
    })

</script>

<div class="max-w-4xl mx-auto p-8">
    <h1 class="text-2xl font-bold mb-16">Create New Document</h1>

    <form on:submit|preventDefault={handleSubmit} class="space-y-6">
        <!-- Title -->
        <div>
            <Label for="title">Title</Label>
            <Input
                    id="title"
                    placeholder="Enter document title"
                    bind:value={documentData.title}
                    required
            />
        </div>

        <!-- Description -->
        <div>
            <Label for="description">Description</Label>
            <Textarea
                    id="description"
                    placeholder="Enter document description"
                    bind:value={documentData.description}
                    required
            />
        </div>

        <!-- File Upload -->
        <div>
            <Label for="file">Upload File: <span class="mx-4">{documentData.file ? documentData.file.name : 'No file chosen'}</span> </Label>
            <Input
                    id="file"
                    on:change={handleFileChange}
                    accept=".pdf,.docx,.txt"
                    type="file"
                    required
            />
        </div>

        <div>
            <Label for="trial">Document Type</Label>
            <Select
                    selected={documentTypes.find(docType => docType.id === selectedDocumentTypeId)}
                    onSelectedChange={(selected) => {
                        selected && (selectedDocumentTypeId = Number(selected.value));
                    }}
            >
                <Trigger>
                    <Value placeholder="Select a trial" />
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
            Create Document
        </Button>
    </form>
</div>
