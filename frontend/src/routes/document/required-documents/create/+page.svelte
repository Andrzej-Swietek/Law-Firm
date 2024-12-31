<script lang="ts">
    import {goto} from "$app/navigation";
    import {onMount} from "svelte";
    import { tick } from "svelte";

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

    import {Card} from "$lib/components/ui/card";
    import * as Table from "$lib/components/ui/table/index";
    import * as Pagination from "$lib/components/ui/pagination";
    import ChevronsUpDown from "lucide-svelte/icons/chevrons-up-down";
    import * as Command from "$lib/components/ui/command/index.js"
    import * as Popover from "$lib/components/ui/popover/index";
    import {cn} from "$lib/utils";

    let requiredDocuments = $state<Omit<RequiredDocumentForTrial,'id'>>({
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

    onMount(async()=>{
        const [trialsData, documentsData] = await Promise.all([
            getAllTrials(1,100000),
            getAllDocument(1,100000)
        ]);
        trials = [...trialsData];
        documents = [...documentsData.data.map(doc => doc.document)]
    })
</script>

<div class="max-w-4xl mx-auto p-8 relative">
    <h1 class="text-2xl font-bold mb-8">Add New Required Document</h1>
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