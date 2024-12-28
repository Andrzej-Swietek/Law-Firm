<script lang="ts">
    import {onMount} from "svelte";
    import {goto} from "$app/navigation";
    import { toast } from "svelte-sonner";

    import * as Table from "$lib/components/ui/table/index";
    import * as Pagination from "$lib/components/ui/pagination";

    import PageHeader from "@components/table/PageHeader.svelte";
    import * as Card from "$lib/components/ui/card";
    import {Button} from "$lib/components/ui/button/index";
    import EntityTable from "@components/table/EntityTable.svelte";

    import type { Document } from "$lib/interfaces/document.interface";
    import type {PaginatedResponse} from "$lib/interfaces/response.interface";

    import {deleteDocument} from "$lib/api/document/deleteDocument";
    import {getAllDocument} from "$lib/api/document/getAllDocuments";


    let documents = $state<Partial<Document>[]>([])
    let page = $state<number>(1);
    const LIMIT = 20;
    let totalCount: number = $state<number>(0);

    const headers =  [ "ID", "Type" ,"title", "description", "Path" ];
    const fields = [ "id", "documentType.name", "title", "description", "filePath" ];

    const handleEdit = (id: number) => goto(`/document/edit/${id}`);

    const handleDelete = async (id: number) => {
        const response = await deleteDocument(id);
        const docsData = await getAllDocument(page, LIMIT);

        documents = [ ...docsData.data.map( doc => doc.document ) ]
        totalCount = docsData.totalCount;

        toast.success("Document successfully deleted", {
            description: "Sunday, December 03, 2023 at 9:00 AM",
        });
    }

    const handleChangePage = async (direction: 'up' | 'down') => {
        if (direction == 'up') page = page + 1;
        else page = page - 1 > 0 ? page -1 : 1;
        const docsData = await getAllDocument(page, LIMIT);
        documents = [...docsData.data.map( doc => doc.document )]
        totalCount = docsData.totalCount;
    }

    onMount(async()=>{
        const docsData = await getAllDocument(page, LIMIT);
        documents = [...docsData.data.map( doc => doc.document )]
        totalCount = docsData.totalCount;
    })
</script>


<PageHeader
        title="Document Management"
        description="Manage Document - CRUD"
        onCreate={ ()=> goto("/document/create") }
/>
<Card.Root class="-mt-8 mb-16">
    <div class="p-6 flex items-center justify-between  rounded-lg shadow-sm">
        <div>
            <h1 class="text-2xl font-bold text-gray-800">Manage Document Types</h1>
            <p class="text-sm text-gray-600 mt-2">Each document has its type that helps organize and filter documents and files </p>
        </div>

        <div>
            <Button onclick={ ()=> goto("/document/document-type") } variant="outline"> View All </Button>
            <Button onclick={ ()=> goto("/document/document-type/create") } variant="outline"> Create New </Button>
        </div>
    </div>
</Card.Root>

<EntityTable
        data={documents}
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
                { page }/{Math.ceil(totalCount/LIMIT)}
            </Pagination.Link>
        </Pagination.Item>
        <Pagination.Item>
            <Pagination.NextButton on:click={ ()=>handleChangePage('up') }/>
        </Pagination.Item>
    </Pagination.Content>
</Pagination.Root>