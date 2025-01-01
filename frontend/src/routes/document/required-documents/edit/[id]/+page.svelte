<script lang="ts">
    import {goto} from "$app/navigation";
    import {onMount} from "svelte";
    import { tick } from "svelte";
    import { page } from '$app/stores';

    import { toast } from "svelte-sonner";
    import {Label} from "$lib/components/ui/label/index";
    import {Input} from "$lib/components/ui/input/index";
    import { Button } from "$lib/components/ui/button";
    import Check from "lucide-svelte/icons/check";

    import type {Trial} from "$lib/interfaces/trial.interface";
    import type { Document } from "$lib/interfaces/document.interface";
    import type {RequiredDocumentForTrial} from "$lib/interfaces/document.interface";

    import {createRequiredDocumentForTrial} from "$lib/api/document/requiredDocument/createRequiredDocument";
    import {getAllTrials} from "$lib/api/trial/getAllTrials";
    import {getAllDocument} from "$lib/api/document/getAllDocuments";
    import {getRequiredDocumentById} from "$lib/api/document/requiredDocument/getRequiredDocuemntById";

    import {Card, Content, Description, Header, Title} from "$lib/components/ui/card";
    import * as Table from "$lib/components/ui/table/index";
    import * as Pagination from "$lib/components/ui/pagination";
    import ChevronsUpDown from "lucide-svelte/icons/chevrons-up-down";
    import * as Command from "$lib/components/ui/command/index.js"
    import * as Popover from "$lib/components/ui/popover/index";
    import {cn} from "$lib/utils";
    import {downloadFileFromStorage} from "$lib/api/storage/downloadFileFromStorage";

    let requiredDocuments = $state<RequiredDocumentForTrial>({
        id: $page.params.id,
        trialId: -1,
        documentId: -1
    })

    let trials = $state<Trial[]>([]);
    let documents = $state<Partial<Document>[]>([]);

    let selectedTrial = $state("Select a trial...");
    let selectedDocument = $state("Select a document...");

    const closeAndFocusTrigger = (triggerId: string) => {
        tick().then(() => {
            document.getElementById(triggerId)?.focus();
        });
    };

    const handleSubmit = async () => {
        try {
            const newRequiredDocuments = await createRequiredDocumentForTrial(requiredDocuments);
            if (newRequiredDocuments) {
                toast.success("Required documents successfully created!");
                goto("/document/required-documents");
                return;
            }
            toast.error("Failed to create required document. Please try again.");
        } catch (error) {
            console.error("Error creating client:", error);
            toast.error("Failed to create required document. Please try again.");
        }
    };

    const downloadFile = async() => {
        if (requiredDocuments?.document?.filePath)
            await downloadFileFromStorage(requiredDocuments?.document?.filePath)
    }

    onMount(async()=>{
        const [trialsData, documentsData, requiredData] = await Promise.all([
            getAllTrials(1,100000),
            getAllDocument(1,100000),
            getRequiredDocumentById($page.params.id)
        ]);
        trials = [...trialsData];
        documents = [...documentsData.data.map(doc => doc.document)];
        requiredDocuments = { ...requiredData! };
        selectedTrial = requiredData?.trial?.title.toString() ?? 'Select a trial...'
        selectedDocument = requiredData?.document?.title.toString() ?? 'Select a document ...'
    })
</script>

<div class="max-w-4xl mx-auto p-8 relative">
    <h1 class="text-2xl font-bold mb-8">Update Required Document</h1>
    <Card class="p-6">
        <form
                class="space-y-4"
                on:submit|preventDefault={() => handleSubmit()}
        >
            <Popover.Root let:open let:ids>
                <Popover.Trigger asChild let:builder>
                    <Button
                            builders={[builder]}
                            variant="outline"
                            role="combobox"
                            aria-expanded={open}
                            class="w-full justify-between"
                    >
                        {selectedTrial}
                        <ChevronsUpDown class="ml-2 h-4 w-4 shrink-0 opacity-50" />
                    </Button>
                </Popover.Trigger>
                <Popover.Content class="w-[50%] p-0">
                    <Command.Root>
                        <Command.Input placeholder="Search trial..." />
                        <Command.Empty>No trial found.</Command.Empty>
                        <Command.Group>
                            {#each trials as trial}
                                <Command.Item
                                        value={trial.id.toString()}
                                        onSelect={(currentValue) => {
                                            requiredDocuments.trialId = parseInt(currentValue);
                                            selectedTrial = trials.find(t => t.id.toString() === currentValue)?.title ?? "Select a trial...";
                                            closeAndFocusTrigger(ids.trigger);
                                        }}
                                >
                                    <Check
                                            class={cn(
                                            "mr-2 h-4 w-4",
                                            requiredDocuments.trialId.toString() !== trial.id.toString() && "text-transparent"
                                        )}
                                    />
                                    {trial.title}
                                </Command.Item>
                            {/each}
                        </Command.Group>
                    </Command.Root>
                </Popover.Content>
            </Popover.Root>

            <!-- Combo Box for Documents -->
            <Popover.Root let:open let:ids>
                <Popover.Trigger asChild let:builder>
                    <Button
                            builders={[builder]}
                            variant="outline"
                            role="combobox"
                            aria-expanded={open}
                            class="w-full justify-between"
                    >
                        {selectedDocument}
                        <ChevronsUpDown class="ml-2 h-4 w-4 shrink-0 opacity-50" />
                    </Button>
                </Popover.Trigger>
                <Popover.Content class="w-[50%] p-0">
                    <Command.Root>
                        <Command.Input placeholder="Search document..." />
                        <Command.Empty>No document found.</Command.Empty>
                        <Command.Group>
                            {#each documents as document}
                                <Command.Item
                                        value={document?.id?.toString()}
                                        onSelect={(currentValue) => {
                                            requiredDocuments.documentId = parseInt(currentValue);
                                            selectedDocument = documents.find(d => d?.id?.toString() === currentValue)?.title ?? "Select a document...";
                                            closeAndFocusTrigger(ids.trigger);
                                        }}
                                >
                                    <Check
                                            class={cn(
                                            "mr-2 h-4 w-4",
                                            requiredDocuments.documentId.toString() !== document?.id?.toString() && "text-transparent"
                                        )}
                                    />
                                    {document.title}
                                </Command.Item>
                            {/each}
                        </Command.Group>
                    </Command.Root>
                </Popover.Content>
            </Popover.Root>

            <!-- Submit Button -->
            <Button type="submit" class="w-full">
                Save Required Document
            </Button>
        </form>
    </Card>
</div>

<div class="flex flex-row w-full gap-4">

    <Card class="flex-1 p-6">
        <Header>
            <Title>Currently Saved Document</Title>
            <h3 class="text-2xl font-black"> { requiredDocuments.document?.title } </h3>
            <Description>{ requiredDocuments.document?.description }</Description>
        </Header>
        <Content>
            <Description> File: { requiredDocuments.document?.filePath }</Description>
            <Description class="mt-4"> Type:  <span class="py-2 ml-2 px-4 bg-slate-600 rounded-[10px] text-white"> { requiredDocuments.document?.documentType?.name } </span></Description>
        </Content>
        <div class="w-full flex-center gap-4">
                <Button on:click={() => downloadFile()}>
                    Download
                </Button>
            <Button variant="outline" on:click={()=> goto(`/document/edit/${requiredDocuments.documentId}`)}>
                Details
            </Button>
        </div>
    </Card>

    <Card class="flex-1 p-6">
        <Header>
            <Title>Currently Saved Trial</Title>
            <h3 class="text-2xl font-black"> { requiredDocuments.trial?.title } </h3>
            <Description>{ requiredDocuments.trial?.description }</Description>
        </Header>
        <Content>
            <Description>Date: { requiredDocuments.trial?.date }</Description>
            <div class="p-4 my-4 border rounded-[10px]" >
                <p>Case: { requiredDocuments.trial?.case?.name }</p>
                <Description>case description: { requiredDocuments.trial?.case?.description }</Description>
            </div>
            <div class="p-4 my-4 border rounded-[10px]" >
                <p>Judge: { requiredDocuments.trial?.client?.firstName } { requiredDocuments.trial?.client?.lastName }</p>
                <Description>E-mail: { requiredDocuments.trial?.client?.email }</Description>
            </div>
            <div class="p-4 my-4 border rounded-[10px]" >
                <p>Lawyer: { requiredDocuments.trial?.lawyer?.firstName } { requiredDocuments.trial?.lawyer?.lastName }</p>
                <Description>Specialization: { requiredDocuments.trial?.lawyer?.specialization }</Description>
            </div>
            <div class="p-4 my-4 border rounded-[10px]" >
                <p>Judge: { requiredDocuments.trial?.judge?.firstName } { requiredDocuments.trial?.judge?.lastName }</p>
                <Description>Court ID: { requiredDocuments.trial?.judge?.courtDivisionId }</Description>
            </div>

            <div class="w-full flex-center">
                <Button on:click={() => goto(`/trial/edit/${requiredDocuments.trialId}`)}>
                    Details
                </Button>
            </div>
        </Content>
    </Card>

</div>