<script lang="ts">
    import {onMount} from "svelte";
    import { tick } from "svelte";

    import type {Person} from "$lib/interfaces/person.interface";
    import type { Signature } from "$lib/interfaces/signature.interface";

    import {Content, Item, Select, Trigger, Value} from "$lib/components/ui/select";
    import {Button} from "$lib/components/ui/button";
    import {Label} from "$lib/components/ui/label";
    import * as Command from "$lib/components/ui/command/index.js";
    import * as Popover from "$lib/components/ui/popover/index.js";
    import Check from "lucide-svelte/icons/check";
    import ChevronsUpDown from "lucide-svelte/icons/chevrons-up-down";

    import { cn } from "$lib/utils.js";
    import type {RequiredDocumentForTrial} from "$lib/interfaces/document.interface";
    import {getAllRequiredDocuments} from "$lib/api/document/requiredDocument/getAllRequiredDocuments";
    import {Separator} from "$lib/components/ui/separator";

    let signature = $state<Omit<Signature, 'id'>>({
        personId: 0,
        requiredDocumentId: 0,
        role: 'client'
    })

    const roles = ['client', 'lawyer', 'judge']

    let people = $state<Person[]>([]);
    let requiredDocuments = $state<RequiredDocumentForTrial[]>([])


    let documentOpen = $state<boolean>(false);
    let documentValue = "";
    let selectedDocumentValue: string = $state<string>("");

    $effect(()=>{
        selectedDocumentValue = requiredDocuments.find(
            (rd)=> documentValue === rd.document?.title
        )?.document?.title ?? "Select a Required Document"
    })

    const handleSubmit = async () => {

    }

    function closeAndFocusTrigger(triggerId: string) {
        documentOpen = false;
        tick().then(() => {
            document.getElementById(triggerId)?.focus();
        });
    }

    onMount(async()=>{
        const [
            requiredDocumentData
        ] = await Promise.all([
            getAllRequiredDocuments()
        ]);

        requiredDocuments = [...requiredDocumentData];
    })
</script>

<div class="max-w-4xl mx-auto p-8">
    <h1 class="text-2xl font-bold mb-16">Create New Signature</h1>
    <form
            class="space-y-4"
            on:submit|preventDefault={handleSubmit}
    >

        <div>
            <Label for="trial">Type of signatory</Label>
            <Select
                    selected={roles.find((s) => s === signature.role)}
                    onSelectedChange={(selected) => {
                        selected && (signature.role = `${selected.value}`);
                    }}
            >
                <Trigger>
                    <Value placeholder="Select a trial" />
                </Trigger>
                <Content>
                    {#each roles as role}
                        <Item value={role} label={`${role}`} />
                    {/each}
                </Content>
            </Select>
        </div>

        <div class="flex flex-col gap-y-4 w-full">
            <Label>Required Document</Label>
            <Popover.Root bind:open={documentOpen} let:ids>
                <Popover.Trigger asChild let:builder>
                    <Button
                            builders={[builder]}
                            variant="outline"
                            role="combobox"
                            aria-expanded={documentOpen}
                            class="w-[200px] justify-between"
                    >
                        Document: {requiredDocuments.find( rd => rd.id === +selectedDocumentValue )?.document?.title ?? 'Not selected' }
                        , Trial: {requiredDocuments.find( rd => rd.id === +selectedDocumentValue )?.trial?.title ?? 'Not selected'}
                        <ChevronsUpDown class="ml-2 h-4 w-4 shrink-0 opacity-50" />
                    </Button>
                </Popover.Trigger>
                <Popover.Content style="width: 50%" class="p-0">
                    <Command.Root>
                        <Command.Input placeholder="Search Required Documents..." />
                        <Command.Empty>No Required Documents found.</Command.Empty>
                        <Command.Group>
                            {#each requiredDocuments as requiredDoc}
                                <Command.Item
                                        value={`${requiredDoc.id}`}
                                        onSelect={(currentValue) => {
                                          selectedDocumentValue = currentValue;
                                          closeAndFocusTrigger(ids.trigger);
                                        }}
                                        class="flex flex-col items-center justify-center cursor-pointer"
                                >
                                    <div>
                                        Document: {requiredDoc.document?.title}
                                    </div>
                                    <div>
                                        Trial: [{requiredDoc?.trial?.id}] {requiredDoc.trial?.title}
                                    </div>
                                </Command.Item>
                            {/each}
                        </Command.Group>
                    </Command.Root>
                </Popover.Content>
            </Popover.Root>
        </div>

        <!-- Submit Button -->
        <Button type="submit" class="w-full mt-6">
            Create Document
        </Button>
    </form>
</div>