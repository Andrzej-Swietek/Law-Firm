<script lang="ts">
    import {Button} from "$lib/components/ui/button/index.js";
    import LoadingSpinner from "@components/common/LoadingSpinner.svelte";
    import * as Card from "$lib/components/ui/card";
    import * as Dialog from "$lib/components/ui/dialog";

    let { text, onOpen, children } = $props();
    let open = $state<boolean>(false);

    const openModal = async () => {
        onOpen()
    }
</script>

<Dialog.Root>
    <Dialog.Trigger>
        <Button on:click={()=> openModal()}>{text}</Button>
    </Dialog.Trigger>
    <Dialog.Content>
        <Dialog.Header>
            <Dialog.Title class="text-2xl">{text}</Dialog.Title>
            <Dialog.Description>
                {@render children?.()}
            </Dialog.Description>
        </Dialog.Header>
    </Dialog.Content>
</Dialog.Root>