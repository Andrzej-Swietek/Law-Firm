<script lang="ts">
    import {onMount} from "svelte";
    import {goto} from "$app/navigation";
    import { toast } from "svelte-sonner";
    import * as Table from "$lib/components/ui/table/index";
    import * as Pagination from "$lib/components/ui/pagination";

    import PageHeader from "@components/table/PageHeader.svelte";
    import EntityTable from "@components/table/EntityTable.svelte";

    import type {CourtDivision} from "$lib/interfaces/court.interface";

    import {getAllCourts} from "$lib/api/court/getAllCourts";
    import {deleteCourt} from "$lib/api/court/deleteCourt.js";


    let courts = $state<CourtDivision[]>([])

    const handleDeleteClient = async (id: number) => {
        const response = await deleteCourt(id);

        courts = [...await getAllCourts()]
        toast.success("Court successfully deleted", {
            description: "Sunday, December 03, 2023 at 9:00 AM",
        });
    }

    onMount(async ()=>{
        const response = await getAllCourts()
        console.log(response)
        courts = [...response]
    })

    const headers = ["ID", "Name", "City"];
    const fields = ["id", "name", "city"];
</script>

<PageHeader
        title="Court Management"
        description="Manage courts - CRUD"
        onCreate={ ()=> goto("/court/create") }
/>
<EntityTable
        data={courts}
        headers={headers}
        {fields}
        onDelete={handleDeleteClient}
/>
