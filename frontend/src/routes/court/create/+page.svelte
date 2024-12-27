<script lang="ts">

    import {toast} from "svelte-sonner";
    import { goto } from "$app/navigation";

    import {Label} from "$lib/components/ui/label/index";
    import {Input} from "$lib/components/ui/input/index";
    import type {CourtDivision} from "$lib/interfaces/court.interface";

    import {createCourt} from "$lib/api/court/createCourt";
    import {Button} from "$lib/components/ui/button";

    let court: Omit<CourtDivision, 'id'> = {
        name: "",
        city: ""
    }

    const handleSubmit = async () => {
        try {
            const newClient = await createCourt(court);
            if (newClient) {
                toast.success("Court Division successfully created!");
                goto("/court");
                return;
            }
            toast.error("Failed to create court. Please try again.");
        } catch (error) {
            console.error("Error creating court:", error);
            toast.error("Failed to create court. Please try again.");
        }
    };

</script>

<div class="max-w-4xl mx-auto p-8">
    <h1 class="text-2xl font-bold mb-16">Create New Client</h1>
    <form
            class="space-y-4"
            on:submit|preventDefault={() => handleSubmit()}
    >
        <!-- Client Information -->
        <div>
            <Label for="name">Court Division Name</Label>
            <Input
                    id="name"
                    type="text"
                    placeholder="Enter court name"
                    bind:value={court.name}
                    required
            />
        </div>
        
        <div>
            <Label for="city">City</Label>
            <Input
                    id="city"
                    type="text"
                    placeholder="Enter city"
                    bind:value={court.city}
                    required
            />
        </div>

        <!-- Submit Button -->
        <Button type="submit" class="w-full mt-6">
            Create Court
        </Button>
    </form>
</div>