<script lang="ts">

import LoadingSpinner from "@components/common/LoadingSpinner.svelte";
import {goto} from "$app/navigation";
import {onMount} from "svelte";
import * as Table from "$lib/components/ui/table/index";
import * as Pagination from "$lib/components/ui/pagination";
import { toast } from "svelte-sonner";

import PageHeader from "@components/table/PageHeader.svelte";
import EntityTable from "@components/table/EntityTable.svelte";

import type {RequiredDocumentForTrial} from "$lib/interfaces/document.interface";
import {deleteRequiredDocument} from "$lib/api/document/requiredDocument/deleteRequiredDocument";
import {getAllRequiredDocuments} from "$lib/api/document/requiredDocument/getAllRequiredDocuments";

let requiredDocuments = $state<RequiredDocumentForTrial[]>([])
let page = $state<number>(1);
let loading = $state<boolean>(true);
const LIMIT = 20;


const handleChangePage = async (direction: 'up' | 'down') => {
    if (direction == 'up') page = page + 1;
    else page = page - 1 > 0 ? page -1 : 1;
     await syncList();
}

const handleDelete = async(id: number) => {
    const response = await deleteRequiredDocument(id);
    toast.success("Requirement of the Document successfully deleted", {
        description: "Sunday, December 03, 2023 at 9:00 AM",
    });
    await syncList();
}

const handleEdit = async(id: number) => {
    goto(`/document/required-documents/edit/${id}`)
}

const syncList = async () => {
    loading = true;
    const response = await getAllRequiredDocuments()
    requiredDocuments = [...response]
    loading = false;
}

onMount(async ()=>{
   await syncList();
})

const headers = ['ID', 'Trial', 'Document', 'Trial Status'];
const fields = ['id', 'trial.title', 'document.title', 'trial.trialStatus.name']
</script>


<PageHeader
        title="Required Documents For Trial Management"
        description="List the required documents for a given trial"
        onCreate={ ()=> goto("/document/required-documents/create") }
/>
{#if loading}
    <div class="w-full flex-center !min-h-[50vh] my-16">
        <LoadingSpinner width="150px" height="150px" color="rgb(55 65 81/.7)" />
    </div>
    {:else}
    <EntityTable
            data={requiredDocuments}
            headers={headers}
            {fields}
            onDelete={handleDelete}
            onEdit={handleEdit}
    />
{/if}

<!--<Pagination.Root count={100} perPage={10} let:pages let:currentPage>-->
<!--    <Pagination.Content>-->
<!--        <Pagination.Item>-->
<!--            <Pagination.PrevButton on:click={ ()=>handleChangePage('down') }/>-->
<!--        </Pagination.Item>-->
<!--        <Pagination.Item>-->
<!--            <Pagination.Link {page}>-->
<!--                { page }-->
<!--            </Pagination.Link>-->
<!--        </Pagination.Item>-->
<!--        <Pagination.Item>-->
<!--            <Pagination.NextButton on:click={ ()=>handleChangePage('up') }/>-->
<!--        </Pagination.Item>-->
<!--    </Pagination.Content>-->
<!--</Pagination.Root>-->