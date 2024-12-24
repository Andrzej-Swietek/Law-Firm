<script lang="ts">
    import * as AlertDialog from "$lib/components/ui/alert-dialog/index";
    import { Button } from "$lib/components/ui/button/index";


    type Props = { text?: string, title?: string, description?: string, onClick: () => void  }
    let {
        text = "Delete",
        title = "Are you absolutely sure?",
        description="Are you sure you want to delete it?",
        onClick
    } = $props();

</script>

<AlertDialog.Root>
    <AlertDialog.Trigger asChild let:builder>
        <Button builders={[builder]} variant="outline">{text}</Button>
    </AlertDialog.Trigger>
    <AlertDialog.Content>
        <AlertDialog.Header>
            <AlertDialog.Title>{ title }</AlertDialog.Title>
            <AlertDialog.Description>
                { description }
            </AlertDialog.Description>
        </AlertDialog.Header>
        <AlertDialog.Footer>
            <AlertDialog.Cancel>Cancel</AlertDialog.Cancel>
            <AlertDialog.Action on:click={()=> onClick()}>Continue</AlertDialog.Action>
        </AlertDialog.Footer>
    </AlertDialog.Content>
</AlertDialog.Root>