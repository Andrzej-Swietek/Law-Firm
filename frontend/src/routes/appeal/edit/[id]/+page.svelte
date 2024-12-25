<script lang="ts">
    import { onMount } from "svelte";
    import { toast } from "svelte-sonner";
    import { createAppeal } from "$lib/api/appeal/createAppeal";
    import { getAllTrials } from "$lib/api/trial/getAllTrials";
    import { getAllRulingsAllPages } from "$lib/api/ruling/getAllRulings";
    import { goto } from "$app/navigation";

    import { Input } from "$lib/components/ui/input";
    import { Label } from "$lib/components/ui/label";
    import { Button } from "$lib/components/ui/button";
    import { Select, Trigger, Value, Content, Item } from "$lib/components/ui/select/index";

    import type { Appeal } from "$lib/interfaces/appeal.interface";
    import type {Trial} from "$lib/interfaces/trial.interface";
    import type {Ruling} from "$lib/interfaces/ruling.interface";

    import { page } from '$app/stores';
    import {getAppealById} from "$lib/api/appeal/getAppealById";

    $: appealId = $page.params.id;

    let appeal: Omit<Appeal, 'id' | 'initialRuling' | 'finalRuling' | 'trial'> = {
        initialRulingId: 0,
        finalRulingId: 0,
        trialId: 0
    };

    let trials: Trial[] = [];
    let rulings: Ruling[] = [];

    let selectedTrialId: number | null = null;
    let selectedInitialRulingId: number | null = null;
    let selectedFinalRulingId: number | null = null;

    onMount(async () => {
        trials = await getAllTrials();
        rulings = await getAllRulingsAllPages();
    });

    const handleSubmit = async () => {
        if (!selectedTrialId || !selectedInitialRulingId || !selectedFinalRulingId) {
            toast.error("Please fill out all fields.");
            return;
        }

        try {
            const newAppeal = await createAppeal({
                ...appeal,
                trialId: selectedTrialId,
                initialRulingId: selectedInitialRulingId,
                finalRulingId: selectedFinalRulingId
            });

            if (newAppeal) {
                toast.success("Appeal successfully created!");
                goto("/appeal");
                return;
            }

            toast.error("Failed to create appeal. Please try again.");
        } catch (error) {
            console.error("Error creating appeal:", error);
            toast.error("Failed to create appeal. Please try again.");
        }
    };

    onMount( async ()=>{
        appealId = Number($page.params.id);
        const appealData = await getAppealById(appealId);
        if (appealData) {
            appeal = {
                ...appealData
            }
        }
    })
</script>

<div class="max-w-4xl mx-auto p-8">
    <h1 class="text-2xl font-bold mb-16">Create New Appeal</h1>
    <form
            class="space-y-4"
            on:submit|preventDefault={handleSubmit}
    >
        <!-- Trial -->
        <div>
            <Label for="trial">Trial</Label>
            <Select
                    selected={trials.find(trial => trial.id === selectedTrialId)}
                    onSelectedChange={(selected) => {
                    selected && (selectedTrialId = Number(selected.value));
                }}
            >
                <Trigger>
                    <Value placeholder="Select a trial" />
                </Trigger>
                <Content>
                    {#each trials as trial}
                        <Item value={trial.id} label={`Trial ${trial.id}`} />
                    {/each}
                </Content>
            </Select>
        </div>

        <!-- Initial Ruling -->
        <div>
            <Label for="initialRuling">Initial Ruling</Label>
            <Select
                    selected={rulings.find(ruling => ruling.id === selectedInitialRulingId)}
                    onSelectedChange={(selected) => {
                    selected && (selectedInitialRulingId = Number(selected.value));
                }}
            >
                <Trigger>
                    <Value placeholder="Select an initial ruling" />
                </Trigger>
                <Content>
                    {#each rulings as ruling}
                        <Item value={ruling.id} label={`Ruling ${ruling.id} - ${ruling.content}`} />
                    {/each}
                </Content>
            </Select>
        </div>

        <!-- Final Ruling -->
        <div>
            <Label for="finalRuling">Final Ruling</Label>
            <Select
                    selected={rulings.find(ruling => ruling.id === selectedFinalRulingId)}
                    onSelectedChange={(selected) => {
                    selected && (selectedFinalRulingId = Number(selected.value));
                }}
            >
                <Trigger>
                    <Value placeholder="Select a final ruling" />
                </Trigger>
                <Content>
                    {#each rulings as ruling}
                        <Item value={ruling.id} label={`Ruling ${ruling.id} - ${ruling.content}`} />
                    {/each}
                </Content>
            </Select>
        </div>

        <!-- Submit Button -->
        <Button type="submit" class="w-full mt-6">
            Create Appeal
        </Button>
    </form>
</div>
