<script lang="ts">
    import { onMount } from "svelte";
    import { toast } from "svelte-sonner";
    import { createJudge } from "$lib/api/judge/createJudge";
    import { getAllCourts } from "$lib/api/court/getAllCourts";
    import { goto } from "$app/navigation";

    import type { Judge} from "$lib/interfaces/person.interface";
    import { Input } from "$lib/components/ui/input";
    import { Label } from "$lib/components/ui/label";
    import { Button } from "$lib/components/ui/button";
    import {Select, Trigger, Value, Content, Item} from "$lib/components/ui/select/index";

    import type {CourtDivision} from "$lib/interfaces/court.interface";

    let judge: Omit<Judge, 'id' | 'courtDivisionId'> = {
        firstName: "",
        lastName: "",
    };

    let courtDivisions: CourtDivision[] = [];
    let selectedCourtDivisionId: number | null = null;

    onMount(async () => {
        courtDivisions = await getAllCourts();
    });

    const handleSubmit = async () => {
        if (!selectedCourtDivisionId) {
            toast.error("Please select a court division.");
            return;
        }

        try {
            const newJudge = await createJudge({ ...judge, courtDivisionId: selectedCourtDivisionId });
            if (newJudge) {
                toast.success("Judge successfully created!");
                goto("/judge");
                return;
            }
            toast.error("Failed to create judge. Please try again.");
        } catch (error) {
            console.error("Error creating judge:", error);
            toast.error("Failed to create judge. Please try again.");
        }
    };
</script>

<div class="max-w-4xl mx-auto p-8">
    <h1 class="text-2xl font-bold mb-16">Create New Judge</h1>
    <form
            class="space-y-4"
            on:submit|preventDefault={() => handleSubmit()}
    >
        <!-- Judge Information -->
        <div>
            <Label for="firstName">First Name</Label>
            <Input id="firstName" type="text" placeholder="Enter first name" bind:value={judge.firstName} required />
        </div>
        <div>
            <Label for="lastName">Last Name</Label>
            <Input id="lastName" type="text" placeholder="Enter last name" bind:value={judge.lastName} required />
        </div>

        <!-- Court Division -->
        <!-- Court Division -->
        <div>
            <Label for="courtDivision">Court Division</Label>
            <Select
                    selected={courtDivisions.find(division => division.id === selectedCourtDivisionId)}
                    onSelectedChange={(selected) => {
                    selected && (selectedCourtDivisionId = Number(selected.value));
                }}
            >
                <Trigger>
                    <Value placeholder="Select a court division" />
                </Trigger>
                <Content>
                    {#each courtDivisions as division}
                        <Item value={division.id} label={division.name + " - " + division.city} />
                    {/each}
                </Content>
            </Select>
        </div>
        <!-- Submit Button -->
        <Button type="submit" class="w-full mt-6">
            Create Judge
        </Button>
    </form>
</div>
