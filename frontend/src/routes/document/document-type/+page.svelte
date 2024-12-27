<script lang="ts">
    import {goto} from "$app/navigation";
    import {onMount} from "svelte";
    import * as Table from "$lib/components/ui/table/index";
    import * as Pagination from "$lib/components/ui/pagination";
    import { toast } from "svelte-sonner";

    import PageHeader from "@components/table/PageHeader.svelte";
    import EntityTable from "@components/table/EntityTable.svelte";

    import {deleteJudge} from "$lib/api/judge/deleteJudge";
    import {getAllDocumentTypes} from "$lib/api/document/documentType/getAllDocumentTypes";
    import type {DocumentType} from "$lib/interfaces/document.interface";

    let documentTypes = $state<DocumentType[]>([])
    let page = $state<number>(1)
    const LIMIT = 20;

    const handleDelete = async (id: number) => {
        const response = await deleteJudge(id);

        documentTypes = [...await getAllDocumentTypes()]
        toast.success("Judge successfully deleted", {
            description: "Sunday, December 03, 2023 at 9:00 AM",
        });
    }

    const handleEdit = (id: number) => {}

    onMount(async ()=>{
        const response = await getAllDocumentTypes()
        documentTypes = [...response]
    })

    const headers = ["ID", "name"];
    const fields = ["id", "name"];
</script>

<PageHeader
        title="Document Type Management"
        description="Manage Document Types - CRUD"
        onCreate={ ()=> goto("/document/document-type/create") }
/>
<EntityTable
        data={documentTypes}
        headers={headers}
        {fields}
        onDelete={handleDelete}
        onEdit={handleEdit}
/>