<script lang="ts">
    import * as Table from "$lib/components/ui/table/index";
    import * as Pagination from "$lib/components/ui/pagination";
    import { toast } from "svelte-sonner";
    import DeleteBtn from "@components/table/DeleteBtn.svelte";

    import type {Client} from "$lib/interfaces/person.interface";
    import {onMount} from "svelte";

    import {getAllClients} from "$lib/api/client/getAllClients";
    import {deleteClient} from "$lib/api/client/deleteClient";
    import PageHeader from "@components/table/PageHeader.svelte";
    import {goto} from "$app/navigation";
    import EntityTable from "@components/table/EntityTable.svelte";


    let clients = $state<Client[]>([])
    let page = $state<number>(1)
    const LIMIT = 20;

    const handleDeleteClient = async (id: number) => {
        const response = await deleteClient(id);

        clients = [...await getAllClients(page,LIMIT)]
        toast.success("Client successfully deleted", {
            description: "Sunday, December 03, 2023 at 9:00 AM",
        });
    }

    const handleEditClient = (id: number) => goto(`/client/edit/${id}`);

    const handleChangePage = async (direction: 'up' | 'down') => {
        if (direction == 'up') page = page + 1;
        else page = page - 1 > 0 ? page -1 : 1;
        clients = [...await getAllClients(page,LIMIT)]
    }

    onMount(async ()=>{
        const response = await getAllClients(page,LIMIT)
        console.log(response)
        clients = [...response]
    })

    const headers = ["ID", "First Name", "Last Name", "E-mail", "Phone No."];
    const fields = ["id", "firstName", "lastName", "email", "contactDetails.phoneNumber"];
</script>

<PageHeader
    title="Client Management"
    description="Manage clients"
    onCreate={ ()=> goto("/client/create") }
/>
<EntityTable
        data={clients}
        headers={headers}
        {fields}
        onDelete={handleDeleteClient}
        onEdit={handleEditClient}
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