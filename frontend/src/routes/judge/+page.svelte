<script lang="ts">
    import {goto} from "$app/navigation";
    import {onMount} from "svelte";
    import * as Table from "$lib/components/ui/table/index";
    import * as Pagination from "$lib/components/ui/pagination";
    import { toast } from "svelte-sonner";

    import type {Judge} from "$lib/interfaces/person.interface";

    import PageHeader from "@components/table/PageHeader.svelte";
    import EntityTable from "@components/table/EntityTable.svelte";

    import {getAllJudges} from "$lib/api/judge/getAllJudges";
    import {deleteJudge} from "$lib/api/judge/deleteJudge";

    let judges = $state<Judge[]>([])
    let page = $state<number>(1)
    const LIMIT = 20;

    const handleDeleteClient = async (id: number) => {
        const response = await deleteJudge(id);

        judges = [...await getAllJudges(page,LIMIT)]
        toast.success("Judge successfully deleted", {
            description: "Sunday, December 03, 2023 at 9:00 AM",
        });
    }

    const handleEdit = (id: number) => goto(`/judge/edit/${id}`);


    const handleChangePage = async (direction: 'up' | 'down') => {
        if (direction == 'up') page = page + 1;
        else page = page - 1 > 0 ? page -1 : 1;
        judges = [...await getAllJudges(page,LIMIT)]
    }

    onMount(async ()=>{
        const response = await getAllJudges(page,LIMIT)
        judges = [...response]
    })

    const headers = ["ID", "First Name", "Last Name", "Court ID", "Division Name", "Court City"];
    const fields = ["id", "firstName", "lastName", "courtDivisionId", "courtDivision.name", "courtDivision.city"];
</script>

<PageHeader
        title="Judges Management"
        description="Manage Judges - CRUD"
        onCreate={ ()=> goto("/judge/create") }
/>
<EntityTable
        data={judges}
        headers={headers}
        {fields}
        onDelete={handleDeleteClient}
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