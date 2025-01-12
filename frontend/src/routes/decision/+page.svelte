<script lang="ts">
    import {onMount} from "svelte";
    import {goto} from "$app/navigation";
    import * as Table from "$lib/components/ui/table/index";
    import * as Pagination from "$lib/components/ui/pagination";
    import { toast } from "svelte-sonner";

    import PageHeader from "@components/table/PageHeader.svelte";
    import EntityTable from "@components/table/EntityTable.svelte";

    import {deleteDecision} from "$lib/api/decision/deleteDecision.js";
    import {getAllDecisions} from "$lib/api/decision/getAllDecisions";

    import type {Decision} from "$lib/interfaces/decision.interface";


    let decisions = $state<Decision[]>([])
    let page = $state<number>(1)
    const LIMIT = 20;

    const handleDelete = async (id: number) => {
        const response = await deleteDecision(id);

        decisions = [...await getAllDecisions(page,LIMIT)]
        toast.success("Client successfully deleted", {
            description: "Sunday, December 03, 2023 at 9:00 AM",
        });
    }

    const handleChangePage = async (direction: 'up' | 'down') => {
        if (direction == 'up') page = page + 1;
        else page = page - 1 > 0 ? page -1 : 1;
        decisions = [...await getAllDecisions(page,LIMIT)]
    }

    onMount(async ()=>{
        const response = await getAllDecisions(page,LIMIT)
        console.log(response)
        decisions = [...response]
    })

    const headers = ["ID", "Name", "Description", "Date", "Case"];
    const fields = ["id", "name", "description", "date", "case.name"];
</script>

<PageHeader
        title="Decisions Management"
        description="Manage decisions"
        onCreate={ ()=> goto("/decision/create") }
/>
<EntityTable
        data={decisions}
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
