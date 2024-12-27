<script lang="ts">
    import { onMount } from 'svelte';
    import {createDocumentType} from "$lib/api/document/documentType/createDocumentType";
    import {Label} from "$lib/components/ui/label";
    import {Input} from "$lib/components/ui/input";
    import {Button} from "$lib/components/ui/button";
    import {toast} from "svelte-sonner";

    let name = '';


    const handleSubmit = async () => {
        try {
            if (!name.trim()) {
                toast.error("Unable to create document type");
                return;
            }

            await createDocumentType({ name });
            toast.success('Document Type created successfully!');
            name = '';
        } catch (error) {
            console.error(error);
            toast.success('Failed to create Document Type.');
        }
    };
</script>

<div class="max-w-md mx-auto mt-10 bg-white p-6 rounded-md shadow-md">
    <h1 class="text-xl font-semibold text-gray-700 mb-4">Create Document Type</h1>

    <div class="space-y-4">
        <div>
            <Label for="name">Name</Label>
            <Input
                    id="name"
                    bind:value={name}
                    placeholder="Enter document type name"
                    class="mt-2"
            />
        </div>

        <div class="mt-4">
            <Button on:click={handleSubmit} class="w-full">
                Save Document Type
            </Button>
        </div>
    </div>
</div>
