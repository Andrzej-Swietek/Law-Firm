<script lang="ts">
    import {goto} from "$app/navigation";
    import {onMount} from "svelte";
    import * as Table from "$lib/components/ui/table/index";
    import * as Pagination from "$lib/components/ui/pagination";
    import { toast } from "svelte-sonner";

    import PageHeader from "@components/table/PageHeader.svelte";
    import EntityTable from "@components/table/EntityTable.svelte";
    import type {Trial} from "$lib/interfaces/trial.interface";

    import {deleteTrial} from "$lib/api/trial/deleteTrial";
    import {getAllTrials} from "$lib/api/trial/getAllTrials";


    let lawyers = $state<Trial[]>([])
    let page = $state<number>(1)
    const LIMIT = 20;

    const handleDelete = async (id: number) => {
        const response = await deleteTrial(id);

        lawyers = [...await getAllTrials(page,LIMIT)]
        toast.success("Trial successfully deleted", {
            description: "Sunday, December 03, 2023 at 9:00 AM",
        });
    }

    const handleEdit = (id: number) => goto(`/trial/edit/${id}`);


    const handleChangePage = async (direction: 'up' | 'down') => {
        if (direction == 'up') page = page + 1;
        else page = page - 1 > 0 ? page -1 : 1;
        lawyers = [...await getAllTrials(page,LIMIT)]
    }

    onMount(async ()=>{
        const response = await getAllTrials(page,LIMIT)
        lawyers = [...response]
    })

    const headers = ["ID", "Title", "Description", "Date" ,"Status", "Lawyer Name", "Lawyer Lastname", "Client Name", "Client Lastname", "Case"];
    const fields = ["id", "title", "description", "date", "trialStatus.name", "lawyer.firstName", "lawyer.lastName", "client.firstName", "client.lastName", "case.name"];
</script>

<PageHeader
        title="Trials Management"
        description="Manage Trials - CRUD"
        onCreate={ ()=> goto("/trial/create") }
/>
<EntityTable
        data={lawyers}
        headers={headers}
        {fields}
        onDelete={handleDelete}
        onEdit={handleEdit}
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