<script lang="ts">
    import {goto} from "$app/navigation";
    import {onMount} from "svelte";
    import * as Table from "$lib/components/ui/table/index";
    import * as Pagination from "$lib/components/ui/pagination";
    import { toast } from "svelte-sonner";

    import PageHeader from "@components/table/PageHeader.svelte";
    import EntityTable from "@components/table/EntityTable.svelte";

    import {deleteSignature} from "$lib/api/signature/deleteSignature";
    import {getAllSignatures} from "$lib/api/signature/getAllSignatures";
    import type {Signature} from "$lib/interfaces/signature.interface";

    let signatures = $state<Signature[]>([])
    let page = $state<number>(1)
    const LIMIT = 20;

    const handleDelete = async (id: number) => {
        const response = await deleteSignature(id);

        signatures = [...await getAllSignatures(page,LIMIT)]
        toast.success("Trial successfully deleted", {
            description: "Sunday, December 03, 2023 at 9:00 AM",
        });
    }

    const handleEdit = (id: number) => goto(`/signature/edit/${id}`);


    const handleChangePage = async (direction: 'up' | 'down') => {
        if (direction == 'up') page = page + 1;
        else page = page - 1 > 0 ? page -1 : 1;
        signatures = [...await getAllSignatures(page,LIMIT)]
    }

    onMount(async ()=>{
        const response = await getAllSignatures(page,LIMIT)
        signatures = [...response]
    })

    const headers = ["ID", "Role", "First Name", "Last Name", "Required Document", "Case"];
    const fields = ["id", "role", "person.firstName", "person.lastName", "requiredDocument.document.title", "requiredDocument.trial.title"];
</script>

<PageHeader
        title="Signatures Management"
        description="Manage Signature - CRUD"
        onCreate={ ()=> goto("/signature/create") }
/>
<EntityTable
        data={signatures}
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
