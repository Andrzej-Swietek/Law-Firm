<script lang="ts">
    import { onMount } from "svelte";
    import { toast } from "svelte-sonner";
    import { goto } from "$app/navigation";

    import {Input} from "$lib/components/ui/input";
    import { Label } from "$lib/components/ui/label";
    import { Button } from "$lib/components/ui/button";
    import {Textarea} from "$lib/components/ui/textarea";
    import {Content, Item, Select, Trigger, Value} from "$lib/components/ui/select";

    import {createCase} from "$lib/api/case/createCase";
    import {getAllClients} from "$lib/api/client/getAllClients";
    import {getAllLawyers} from "$lib/api/lawyer/getAllLawyers";

    import type {Case} from "$lib/interfaces/case.interface";
    import type {Client, Lawyer} from "$lib/interfaces/person.interface";

    let clients = $state<Client[]>([])
    let lawyers = $state<Lawyer[]>([])

    let caseItem = $state<Partial<Case>>({
        name: "",
        description: "",
        responsibleLawyerId: -1,
        clientId: -1,
    })

    const handleSubmit = async () => {
        try {
            const newClient = await createCase(caseItem);
            if (newClient) {
                toast.success("Case successfully created!");
                goto("/case");
                return;
            }
            toast.error("Failed to create case. Please try again.");
        } catch (error) {
            console.error("Error creating case:", error);
            toast.error("Failed to create case. Please try again.");
        }
    };

    onMount(async()=>{
        const [clientsData,lawyersData] = await Promise.all([
            getAllClients(1, 1000000),
            getAllLawyers(1,100000)
        ]);

        clients = [...clientsData];
        lawyers = [...lawyersData];
    })

</script>

<div class="max-w-4xl mx-auto p-8">
    <h1 class="text-2xl font-bold mb-16">Create New Case</h1>
    <form
            class="space-y-4"
            on:submit|preventDefault={() => handleSubmit()}
    >
        <!-- Case Name -->
        <div>
            <Label for="name">Case Name</Label>
            <Input
                    id="name"
                    type="text"
                    placeholder="Enter case name"
                    bind:value={caseItem.name}
                    required
            />
        </div>

        <!-- Description -->
        <div>
            <Label for="description">Description</Label>
            <Textarea
                    id="description"
                    placeholder="Enter document description"
                    bind:value={caseItem.description}
                    required
            />
        </div>

        <div>
            <Label for="trial">Responsible Lawyer</Label>
            <Select
                    selected={lawyers.find(l => l.id === caseItem.responsibleLawyerId)}
                    onSelectedChange={(selected) => {
                        selected && (caseItem.responsibleLawyerId = Number(selected.value));
                    }}
            >
                <Trigger>
                    <Value placeholder="Select a lawyer" />
                </Trigger>
                <Content>
                    {#each lawyers as l}
                        <Item value={l.id} label={`${l.firstName} ${l.lastName} - ${l.specialization}`} />
                    {/each}
                </Content>
            </Select>
        </div>

        <div>
            <Label for="trial">Client</Label>
            <Select
                    selected={clients.find(c => c.id === caseItem.clientId)}
                    onSelectedChange={(selected) => {
                        selected && (caseItem.clientId = Number(selected.value));
                    }}
            >
                <Trigger>
                    <Value placeholder="Select a client" />
                </Trigger>
                <Content>
                    {#each clients as c}
                        <Item value={c.id} label={`${c.firstName} ${c.lastName}`} />
                    {/each}
                </Content>
            </Select>
        </div>

        <!-- Submit Button -->
        <Button type="submit" class="w-full mt-6">
            Create Case
        </Button>

    </form>
</div>