<script lang="ts">
    import { onMount } from "svelte";
    import { toast } from "svelte-sonner";
    import { goto } from "$app/navigation";

    import { Input } from "$lib/components/ui/input";
    import { Label } from "$lib/components/ui/label";
    import { Button } from "$lib/components/ui/button";
    import { Card, CardHeader, CardContent, CardFooter } from "$lib/components/ui/card";

    import type { Client, ContactDetails } from "$lib/interfaces/person.interface";
    import { getClientById } from "$lib/api/client/getClientById";
    import { updateClient } from "$lib/api/client/updateClient";
    import {updateContactDetails} from "$lib/api/client/contactDetails/updateContactDetails";
    import {getContactDetailsByCDId} from "$lib/api/client/contactDetails/getContactDetailsByCDId";

    import { page } from '$app/stores';

    $: clientId = $page.params.id;

    let client: Partial<Client> = {};
    let contactDetails: Partial<ContactDetails> = {};

    onMount(async () => {

        if (isNaN(clientId)) {
            toast.error("Invalid client ID");
            goto("/client");
            return;
        }

        try {
            client = await getClientById(clientId) ?? {};
            contactDetails = client.contactDetailsId
                ? await getContactDetailsByCDId(client.contactDetailsId) ?? {}
                : {};
        } catch (error) {
            toast.error("Failed to load client data");
            console.error(error);
            goto("/client");
        }
    });

    const handleUpdateClient = async () => {
        try {
            const updatedClient = await updateClient(clientId, client);
            if (updatedClient) {
                toast.success("Client updated successfully!");
                return;
            }
            toast.error("Failed to update client.");
        } catch (error) {
            console.error("Error updating client:", error);
            toast.error("Error updating client.");
        }
    };

    const handleUpdateContactDetails = async () => {
        try {
            if (!client.contactDetailsId) return;

            const updatedDetails = await updateContactDetails(client.contactDetailsId, contactDetails);
            if (updatedDetails) {
                toast.success("Contact details updated successfully!");
                return;
            }
            toast.error("Failed to update contact details.");
        } catch (error) {
            console.error("Error updating contact details:", error);
            toast.error("Error updating contact details.");
        }
    };
</script>

<div class="max-w-4xl mx-auto p-8 space-y-12">
    <h1 class="text-2xl font-bold mb-8">Edit Client</h1>

    <!-- Client Form -->
    <Card>
        <CardHeader>
            <h2 class="text-lg font-semibold">Client Information</h2>
        </CardHeader>
        <CardContent>
            <form class="space-y-4" on:submit|preventDefault={() => handleUpdateClient()}>
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
            </form>
        </CardContent>
        <CardFooter>
            <Button class="w-full" on:click={() => handleUpdateClient()}>Save Client</Button>
        </CardFooter>
    </Card>

    <!-- Contact Details Form -->
    <Card>
        <CardHeader>
            <h2 class="text-lg font-semibold">Contact Details</h2>
        </CardHeader>
        <CardContent>
            <form class="space-y-4" on:submit|preventDefault={() => handleUpdateContactDetails()}>
                <div>
                    <Label for="phoneNumber">Phone Number</Label>
                    <Input
                            id="phoneNumber"
                            type="tel"
                            placeholder="Enter phone number"
                            bind:value={contactDetails.phoneNumber}
                            required
                    />
                </div>
                <div>
                    <Label for="street">Street</Label>
                    <Input
                            id="street"
                            type="text"
                            placeholder="Enter street"
                            bind:value={contactDetails.street}
                    />
                </div>
                <div>
                    <Label for="zipCode">ZIP Code</Label>
                    <Input
                            id="zipCode"
                            type="text"
                            placeholder="Enter ZIP Code"
                            bind:value={contactDetails.zipCode}
                    />
                </div>
                <div>
                    <Label for="state">State</Label>
                    <Input
                            id="state"
                            type="text"
                            placeholder="Enter state"
                            bind:value={contactDetails.state}
                    />
                </div>
                <div>
                    <Label for="country">Country</Label>
                    <Input
                            id="country"
                            type="text"
                            placeholder="Enter country"
                            bind:value={contactDetails.country}
                    />
                </div>
            </form>
        </CardContent>
        <CardFooter>
            <Button class="w-full" on:click={() => handleUpdateContactDetails()}>Save Contact Details</Button>
        </CardFooter>
    </Card>
</div>
