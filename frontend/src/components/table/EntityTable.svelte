<script lang="ts">

    import {Button} from "$lib/components/ui/button";

    type Props = {
        data: any[],
        headers: string[],
        fields: string[],
        onDelete?: (id: number) => void,
        onDetails?: (id: number) => void,
        onEdit?: (id: number) => void,
        showDetails?: boolean
    }

    let {
        data,
        headers,
        fields,
        onDelete = () => {} ,
        onDetails = () => {},
        onEdit = () => {},
        showDetails = true
    }: Props = $props();

    import * as Table from "$lib/components/ui/table/index";
    import DeleteBtn from "@components/table/DeleteBtn.svelte";

    const getNestedValue = (item: any, path: string): any => {
        return path.split('.').reduce((acc, key) => acc && acc[key], item);
    };
</script>

<Table.Root class="mb-8">
    <Table.Header>
        <Table.Row>
            {#each headers as column, i}
                <Table.Head>{column}</Table.Head>
            {/each}
            <Table.Head class="text-right">Actions</Table.Head>
        </Table.Row>
    </Table.Header>
    <Table.Body>
        {#if data.length > 0}
            {#each data as item}
                <Table.Row>
                    {#each fields as field}
                        <Table.Cell>{ field.split(".").length === 1? item[field] : getNestedValue(item, field)}</Table.Cell>
                    {/each}
                    <Table.Cell class="text-right">
                        {#if showDetails}
                            <Button on:click={() => onDetails(item.id)} variant="outline">Details</Button>
                        {/if}
                        <Button on:click={() => onEdit(item.id)} variant="outline">Edit</Button>
                        <DeleteBtn onClick={() => onDelete(item.id)} />
                    </Table.Cell>
                </Table.Row>
            {/each}
        {:else}
            <Table.Row>
                <Table.Cell colspan="100%" class="text-center py-4 font-black">
                    No Records in database
                </Table.Cell>
            </Table.Row>
        {/if}
    </Table.Body>
</Table.Root>
