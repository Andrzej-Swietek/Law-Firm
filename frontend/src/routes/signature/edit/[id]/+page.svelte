<script lang="ts">
    import {onMount} from "svelte";
    import { tick } from "svelte";
    import {toast} from "svelte-sonner";
    import { page } from '$app/stores';
    import { goto } from "$app/navigation";

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
    import {getAllClients} from "$lib/api/client/getAllClients";
    import {getAllLawyers} from "$lib/api/lawyer/getAllLawyers";
    import {getAllJudges} from "$lib/api/judge/getAllJudges";
    import {createSignature} from "$lib/api/signature/createSignature";
    import {getSignatureById} from "$lib/api/signature/getSignatureById";
    import {updateSignature} from "$lib/api/signature/updateSignature";


    let signature = $state<Signature>({
        id: 0,
        personId: 0,
        requiredDocumentId: 0,
        role: 'client'
    })

    const roles = ['client', 'lawyer', 'judge']

    let people = $state<Person[]>([]);
    let requiredDocuments = $state<RequiredDocumentForTrial[]>([])


    let documentOpen = $state<boolean>(false);
    let documentValue = $state<string>("");
    let selectedDocumentValue: string = $state<string>("");

    let personOpen = $state<boolean>(false);
    let personValue = $state<string>("");
    let selectedPersonValue: string = $state<string>("");


    $effect(()=>{
        selectedDocumentValue = requiredDocuments.find(
            (rd)=> documentValue === rd.document?.title
        )?.document?.title ?? "Select a Required Document"
    })

    $effect(()=>{
        const person = people.find((p)=> personValue === `${p.firstName} ${p.lastName}`)
        selectedPersonValue = person ? `${person.firstName} ${person.lastName}` : "Select a Document Signer"
    })

    const handleSubmit = async () => {
        const response = await updateSignature($page.params.id, {
            id: $page.params.id,
            personId: +selectedPersonValue,
            role: signature.role,
            requiredDocumentId: +selectedDocumentValue,
        });

        if (response) {
            toast.success("Signature successfully updated!");
        } else {
            toast.error("Failed to update signature. Please try again.");
        }
    }

    function closeAndFocusTrigger(triggerId: string) {
        documentOpen = false;
        personOpen = false;
        tick().then(() => {
            document.getElementById(triggerId)?.focus();
        });
    }

    $effect(()=>{
        (async()=>{
            switch (signature.role) {
                case 'client':
                    people = [ ...await getAllClients(1,1000000) ];
                    break;
                case 'lawyer':
                    people = [ ...await getAllLawyers(1,1000000) ];
                    break;
                case 'judge':
                    people = [ ...await getAllJudges(1, 1000000) ];
                    break;
            }
        })()
    })

    const getSelectedPerson = () => people.find( p => p.id === +selectedPersonValue )

    onMount(async()=>{
        const [
            requiredDocumentData,
            signatureData
        ] = await Promise.all([
            getAllRequiredDocuments(),
            getSignatureById($page.params.id)
        ]);

        requiredDocuments = [...requiredDocumentData];
        if (!signatureData) goto('/signature')
        signature = {...signatureData!}

        selectedDocumentValue = signatureData?.requiredDocumentId?.toString() ?? "";
        documentValue = signatureData?.requiredDocument?.document?.title ?? '';
        selectedPersonValue = signatureData?.personId?.toString() ?? "";
        personValue = signatureData?.person?.firstName + " " + signatureData?.person?.lastName
    })
</script>



<div class="max-w-4xl mx-auto p-8">
    <h1 class="text-2xl font-bold mb-16">Edit Signature</h1>
    <form
            class="space-y-4"
            on:submit|preventDefault={handleSubmit}
    >

        <div>
            <Label for="trial">Type of signatory | Current: {signature.role}</Label>
            <Select
                    selected={roles.find((s) => s === signature.role)}
                    onSelectedChange={(selected) => {
                        selected && (signature.role = `${selected.value}`);
                    }}
            >
                <Trigger>
                    <Value placeholder="Select a type of signatory" />
                </Trigger>
                <Content>
                    {#each roles as role}
                        <Item value={role} label={`${role}`} />
                    {/each}
                </Content>
            </Select>
        </div>

        <!-- Required Document -->
        <div class="flex flex-col gap-y-4 w-full">
            <Label>Required Document | {signature?.requiredDocument?.document?.title} {signature?.requiredDocument?.trial?.title}</Label>
            <Popover.Root bind:open={documentOpen} let:ids>
                <Popover.Trigger asChild let:builder>
                    <Button
                            builders={[builder]}
                            variant="outline"
                            role="combobox"
                            aria-expanded={documentOpen}
                            class="w-full justify-between"
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

        <div class="flex flex-col gap-y-4 w-full">
            <Label>Document Signer | Current: { signature.person?.firstName }  { signature.person?.lastName }</Label>
            <Popover.Root bind:open={personOpen} let:ids>
                <Popover.Trigger asChild let:builder>
                    <Button
                            builders={[builder]}
                            variant="outline"
                            role="combobox"
                            aria-expanded={personOpen}
                            class="w-full justify-between"
                    >
                        {
                            getSelectedPerson() ? `${ getSelectedPerson()?.firstName } ${ getSelectedPerson()?.lastName }` : 'Not Selected'
                        }
                        <ChevronsUpDown class="ml-2 h-4 w-4 shrink-0 opacity-50" />
                    </Button>
                </Popover.Trigger>
                <Popover.Content style="width: 50%" class="p-0">
                    <Command.Root>
                        <Command.Input placeholder="Search People ..." />
                        <Command.Empty>No Person found.</Command.Empty>
                        <Command.Group>
                            {#each people as person}
                                <Command.Item
                                        value={`${person.id}`}
                                        onSelect={(currentValue) => {
                                          selectedPersonValue = currentValue;
                                          closeAndFocusTrigger(ids.trigger);
                                        }}
                                        class="flex flex-col items-center justify-center cursor-pointer"
                                >
                                    {person.firstName} {person.lastName}
                                </Command.Item>
                            {/each}
                        </Command.Group>
                    </Command.Root>
                </Popover.Content>
            </Popover.Root>
        </div>

        <!-- Submit Button -->
        <Button type="submit" class="w-full mt-6">
            Save Signature
        </Button>
    </form>
</div>