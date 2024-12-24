<script lang="ts">
    import * as Card from "$lib/components/ui/card";

    import {Button} from "$lib/components/ui/button";

    type Props = {
        title: string,
        description: string,
        onCreate: () => void
    }
    let { title, description, onCreate }: Props = $props();
</script>

<Card.Root class="mb-16">
    <div class="p-6 flex items-center justify-between  rounded-lg shadow-sm">
        <div>
            <h1 class="text-2xl font-bold text-gray-800">{title}</h1>
            <p class="text-sm text-gray-600 mt-2">{description}</p>
        </div>

        <Button onclick={ ()=> onCreate() } variant="outline"> Create New </Button>
    </div>
</Card.Root>