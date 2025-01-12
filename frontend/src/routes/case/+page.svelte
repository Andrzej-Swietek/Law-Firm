<script lang="ts">
    import {onMount} from "svelte";
    import {goto} from "$app/navigation";
    import * as Table from "$lib/components/ui/table/index";
    import * as Pagination from "$lib/components/ui/pagination";
    import { toast } from "svelte-sonner";

    import PageHeader from "@components/table/PageHeader.svelte";
    import EntityTable from "@components/table/EntityTable.svelte";

   import {deleteCase} from "$lib/api/case/deleteCase";
   import {getAllCases} from "$lib/api/case/getAllCases";

    import type {Case} from "$lib/interfaces/case.interface";


    let cases = $state<Case[]>([])
    let page = $state<number>(1)
    const LIMIT = 20;

    const handleDelete = async (id: number) => {
        const response = await deleteCase(id);

        cases = [...await getAllCases(page, LIMIT)]
        toast.success("Client successfully deleted", {
            description: "Sunday, December 03, 2023 at 9:00 AM",
        });
    }

    const handleChangePage = async (direction: 'up' | 'down') => {
        if (direction == 'up') page = page + 1;
        else page = page - 1 > 0 ? page -1 : 1;
        cases = [...await getAllCases(page,LIMIT)]
    }

    onMount(async ()=>{
        const response = await getAllCases(page,LIMIT)
        cases = [...response]
    })

    const headers = ["ID", "Name", "Description", "Client Name", "Client Lastname", "Lawyer Name", "Lawyer Lastname"];
    const fields = ["id", "name", "description", "client.firstName", "client.lastName", "responsibleLawyer.firstName", "responsibleLawyer.lastName"];
</script>

<PageHeader
        title="Decisions Management"
        description="Manage decisions"
        onCreate={ ()=> goto("/cases/create") }
/>
<EntityTable
        data={cases}
        headers={headers}
        {fields}
        onDelete={handleDelete}
        onEdit={(id)=>goto(`/case/edit/${id}`)}
        onDetails={(id)=>goto(`/case/${id}`)}
/>
<Pagination.Root count={100} perPage={10} let:pages let:currentPage>
    <Pagination.Content>
        <Pagination.Item>
            <Pagination.PrevButton on:click={ ()=>handleChangePage('down') }/>
        </Pagination.Item>
        <Pagination.Item>
            <Pagination.Link {page}>
                { page }
            </Pagination.Link>
        </Pagination.Item>
        <Pagination.Item>
            <Pagination.NextButton on:click={ ()=>handleChangePage('up') }/>
        </Pagination.Item>
    </Pagination.Content>
</Pagination.Root>
