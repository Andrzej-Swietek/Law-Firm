<script lang="ts">
    import { onMount } from "svelte";
    import { toast } from "svelte-sonner";
    import { createLawyer } from "$lib/api/lawyer/createLawyer";
    import { goto } from "$app/navigation";

    import type { Lawyer } from "$lib/interfaces/person.interface";
    import { Input } from "$lib/components/ui/input";
    import { Label } from "$lib/components/ui/label";
    import { Button } from "$lib/components/ui/button";

    let lawyer: Omit<Lawyer, 'id'> = {
        firstName: "",
        lastName: "",
        specialization: "",
    };

    const handleSubmit = async () => {
        try {
            const newLawyer = await createLawyer(lawyer);
            if (newLawyer) {
                toast.success("Lawyer successfully created!");
                goto("/lawyer");
                return;
            }
            toast.error("Failed to create lawyer. Please try again.");
        } catch (error) {
            console.error("Error creating lawyer:", error);
            toast.error("Failed to create lawyer. Please try again.");
        }
    };
</script>

<div class="max-w-4xl mx-auto p-8">
    <h1 class="text-2xl font-bold mb-16">Create New Lawyer</h1>
    <form
            class="space-y-4"
            on:submit|preventDefault={() => handleSubmit()}
    >
        <!-- Lawyer Information -->
        <div>
            <Label for="firstName">First Name</Label>
            <Input id="firstName" type="text" placeholder="Enter first name" bind:value={lawyer.firstName} required />
        </div>
        <div>
            <Label for="lastName">Last Name</Label>
            <Input id="lastName" type="text" placeholder="Enter last name" bind:value={lawyer.lastName} required />
        </div>
        <div>
            <Label for="specialization">Specialization</Label>
            <Input id="specialization" type="text" placeholder="Enter specialization" bind:value={lawyer.specialization} required />
        </div>

        <!-- Submit Button -->
        <Button type="submit" class="w-full mt-6">
            Create Lawyer
        </Button>
    </form>
</div>
