<script lang="ts">
    import {goto} from "$app/navigation";
    import {onMount} from "svelte";
    import * as Table from "$lib/components/ui/table/index";
    import * as Pagination from "$lib/components/ui/pagination";
    import { toast } from "svelte-sonner";

    import type {Lawyer} from "$lib/interfaces/person.interface";

    import PageHeader from "@components/table/PageHeader.svelte";
    import EntityTable from "@components/table/EntityTable.svelte";

    import {getAllLawyers} from "$lib/api/lawyer/getAllLawyers";
    import {deleteLawyer} from "$lib/api/lawyer/deleteLawyer"

    let lawyers = $state<Lawyer[]>([])
    let page = $state<number>(1)
    const LIMIT = 20;

    const handleDelete = async (id: number) => {
        const response = await deleteLawyer(id);

        lawyers = [...await getAllLawyers(page,LIMIT)]
        toast.success("Lawyers successfully deleted", {
            description: "Sunday, December 03, 2023 at 9:00 AM",
        });
    }

    const handleEdit = (id: number) => goto(`/lawyer/edit/${id}`);


    const handleChangePage = async (direction: 'up' | 'down') => {
        if (direction == 'up') page = page + 1;
        else page = page - 1 > 0 ? page -1 : 1;
        lawyers = [...await getAllLawyers(page,LIMIT)]
    }

    onMount(async ()=>{
        const response = await getAllLawyers(page,LIMIT)
        lawyers = [...response]
    })

    const headers = ["ID", "First Name", "Last Name", "Specialization"];
    const fields = ["id", "firstName", "lastName", "specialization"];
</script>

<PageHeader
        title="Lawyers Management"
        description="Manage Lawyers - CRUD"
        onCreate={ ()=> goto("/lawyer/create") }
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