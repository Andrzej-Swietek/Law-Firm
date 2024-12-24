<script lang="ts">
    import {onMount} from "svelte";
    import {goto} from "$app/navigation";
    import * as Table from "$lib/components/ui/table/index";
    import * as Pagination from "$lib/components/ui/pagination";
    import { toast } from "svelte-sonner";

    import PageHeader from "@components/table/PageHeader.svelte";
    import EntityTable from "@components/table/EntityTable.svelte";

    import {getAllRulings} from "$lib/api/ruling/getAllRulings";
    import {deleteRuling} from "$lib/api/ruling/deleteRuling";

    import type {Ruling} from "$lib/interfaces/ruling.interface";

    let rulings = $state<Ruling[]>([])
    let page = $state<number>(1)
    const LIMIT = 20;

    const handleDelete = async (id: number) => {
        const response = await deleteRuling(id);

        rulings = [...await getAllRulings(page, LIMIT)]
        toast.success("Ruling successfully deleted", {
            description: "Sunday, December 03, 2023 at 9:00 AM",
        });
    }

    const handleChangePage = async (direction: 'up' | 'down') => {
        if (direction == 'up') page = page + 1;
        else page = page - 1 > 0 ? page -1 : 1;
        rulings = [...await getAllRulings(page,LIMIT)]
    }

    onMount(async ()=>{
        const response = await getAllRulings(page,LIMIT)
        rulings = [...response]
    })

    const headers = ["ID", "Is Final", "Content", "Date", "Trial Title"];
    const fields = ["id", "isFinal", "content", "finalizationDate", "trial.title"];
</script>

<PageHeader
        title="Appeals Management"
        description="Manage appeals"
        onCreate={ ()=> goto("/appeal/create") }
/>
<EntityTable
        data={rulings}
        headers={headers}
        {fields}
        onDelete={handleDelete}
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
