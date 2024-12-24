<script lang="ts">
    import { onMount } from "svelte";
    import { toast } from "svelte-sonner";
    import { createClient } from "$lib/api/client/createClient";
    import { goto } from "$app/navigation";


    import type {Client, ContactDetails} from "$lib/interfaces/person.interface";

    import {Input} from "$lib/components/ui/input";
    import { Label } from "$lib/components/ui/label";
    import { Button } from "$lib/components/ui/button";


    // Form data
    type ClientFormData = Omit<Client, 'id' | 'contactDetailsId'> & { contactDetails: Omit<ContactDetails, 'id'> };

    let client: ClientFormData = {
        firstName: "",
        lastName: "",
        email: "",
        contactDetails: {
            phoneNumber: "",
            email: "",
            street: "",
            city: "",
            state: "",
            zipCode: "",
            country: ""
        },
    };

    $: client.contactDetails!.email = client.email;

    const handleSubmit = async () => {
        try {
            const newClient = await createClient(client);
            if (newClient) {
                toast.success("Client successfully created!");
                goto("/client");
                return;
            }
            toast.error("Failed to create client. Please try again.");
        } catch (error) {
            console.error("Error creating client:", error);
            toast.error("Failed to create client. Please try again.");
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
            <Label for="firstName">First Name</Label>
            <Input
                    id="firstName"
                    type="text"
                    placeholder="Enter first name"
                    bind:value={client.firstName}
                    required
            />
        </div>
        <div>
            <Label for="lastName">Last Name</Label>
            <Input
                    id="lastName"
                    type="text"
                    placeholder="Enter last name"
                    bind:value={client.lastName}
                    required
            />
        </div>
        <div>
            <Label for="email">Email</Label>
            <Input
                    id="email"
                    type="email"
                    placeholder="Enter email"
                    bind:value={client.email}
                    required
            />
        </div>

        <!-- Contact Details -->
        <div>
            <Label for="phoneNumber">Phone Number</Label>
            <Input
                    id="phoneNumber"
                    type="tel"
                    placeholder="Enter phone number"
                    bind:value={client.contactDetails.phoneNumber}
                    required
            />
        </div>
        <div>
            <Label for="street">Street</Label>
            <Input
                    id="street"
                    type="text"
                    placeholder="Enter street"
                    bind:value={client.contactDetails.street}
            />
        </div>
        <div>
            <Label for="zipCode">ZIP Code</Label>
            <Input
                    id="zipCode"
                    type="text"
                    placeholder="Enter ZIP Code"
                    bind:value={client.contactDetails.zipCode}
            />
        </div>
        <div>
            <Label for="state">State</Label>
            <Input
                    id="state"
                    type="text"
                    placeholder="Enter state"
                    bind:value={client.contactDetails.state}
            />
        </div>
        <div>
            <Label for="country">Country</Label>
            <Input
                    id="country"
                    type="text"
                    placeholder="Enter country"
                    bind:value={client.contactDetails.country}
            />
        </div>


        <!-- Submit Button -->
        <Button type="submit" class="w-full mt-6">
            Create Client
        </Button>
    </form>
</div>
