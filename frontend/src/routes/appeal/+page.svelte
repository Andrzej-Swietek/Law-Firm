<script lang="ts">
    import {onMount} from "svelte";
    import {goto} from "$app/navigation";
    import * as Table from "$lib/components/ui/table/index";
    import * as Pagination from "$lib/components/ui/pagination";
    import { toast } from "svelte-sonner";

    import PageHeader from "@components/table/PageHeader.svelte";
    import EntityTable from "@components/table/EntityTable.svelte";

    import {deleteAppeal} from "$lib/api/appeal/deleteAppeal";
    import { getAllAppeals } from "$lib/api/appeal/getAllAppeals";

    import type {Appeal} from "$lib/interfaces/appeal.interface";




    let appeals = $state<Appeal[]>([])
    let page = $state<number>(1)
    const LIMIT = 20;

    const handleDelete = async (id: number) => {
        const response = await deleteAppeal(id);

        appeals = [...await getAllAppeals(page, LIMIT)]
        toast.success("Appeal successfully deleted", {
            description: "Sunday, December 03, 2023 at 9:00 AM",
        });
    }

    const handleChangePage = async (direction: 'up' | 'down') => {
        if (direction == 'up') page = page + 1;
        else page = page - 1 > 0 ? page -1 : 1;
        appeals = [...await getAllAppeals(page,LIMIT)]
    }

    onMount(async ()=>{
        const response = await getAllAppeals(page,LIMIT)
        appeals = [...response]
    })

    const headers = ["ID", "Trial", "Description", 'Initial Ruling Trial', 'Final Ruling Trial', "Client Name", "Client Lastname", "Lawyer Name", "Lawyer Lastname"];
    const fields = ["id", "trial.title", "trial.description", 'initialRuling.content', 'finalRuling.content', "trial.client.firstName", "trial.client.lastName", "trial.lawyer.firstName", "trial.lawyer.lastName"];
</script>

<PageHeader
        title="Appeals Management"
        description="Manage appeals"
        onCreate={ ()=> goto("/appeal/create") }
/>
<EntityTable
        data={appeals}
        headers={headers}
        {fields}
        onDelete={handleDelete}
        onDetails={(id)=> goto(`/appeal/${id}`)}
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
