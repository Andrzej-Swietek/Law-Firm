<script lang="ts">
    import {onMount} from "svelte";
    import { toast } from "svelte-sonner";
    import {goto} from "$app/navigation";
    import { page } from '$app/stores';

    import {Button} from "$lib/components/ui/button";
    import PageHeader from "@components/table/PageHeader.svelte";
    import * as Card from "$lib/components/ui/card";
    import * as Dialog from "$lib/components/ui/dialog";

    import LoadingSpinner from "@components/common/LoadingSpinner.svelte";


    import type {Decision} from "$lib/interfaces/decision.interface";
    import {getDecisionById, getDecisionsByCaseId} from "$lib/api/decision/getDecisionById";

    let decision = $state<Decision>()
    let previousDecisions = $state<Decision[]>([])

    onMount(async()=>{
        const [decisionData] = await Promise.all([
            getDecisionById($page.params.id)
        ]);
        if ( decisionData ) decision = {...decisionData};
        else goto(`/decision`)

        const result = await getDecisionsByCaseId(`${decisionData?.caseId || 0}`)
        previousDecisions = [...result]
    })

</script>

<PageHeader
        title="Case Management"
        description="Manage Case - Details"
        onCreate={ ()=> goto("/case/create") }
/>

{#if decision}
    <div class="max-w-4xl px-8 flex flex-row gap-8 mt-8 mb-8">
        <h1 class="text-2xl font-bold mb-16 w-full flex-1 ">Case ID: {decision.id}</h1>
    </div>

    <div class="flex flex-row flex-wrap gap-8 mb-16">
        <Card.Root class="w-full px-16 py-8">
            <Card.Header>
                <Card.Title class="font-black text-2xl">{ decision.name }</Card.Title>
                <Card.Description class="text-xl mt-4"> {decision.description}  </Card.Description>
            </Card.Header>
            <Card.Content>
                <p class="my-2">Date: <span class="ml-2 font-black"> { decision.date} </span> </p>
                <p class="my-2">Case: <span class="ml-2 font-black"> { decision.case?.name} </span> </p>
                <p class="my-2">Case description: <span class="ml-2 font-black"> { decision.case?.name} </span> </p>
            </Card.Content>
            <Card.Footer class="flex flex-row gap-4">
                <Button on:click={()=> goto(`/case/${decision?.caseId}`)}>Case Details</Button>
            </Card.Footer>
        </Card.Root>
    </div>

    <div class="w-full mt-16 px-8">
        <h2 class="text-2xl font-bold mb-8">Decisions Timeline</h2>

        {#if previousDecisions && previousDecisions.length > 0}
            <ol class="relative border-l border-gray-200 dark:border-gray-700">
                {#each previousDecisions as decision, i}
                    <li class="mb-10 ml-6">
                        <span class="absolute flex items-center justify-center w-8 h-8 bg-green-100 rounded-full -left-4 ring-8 ring-white dark:ring-gray-900 dark:bg-green-900">
                            {i + 1}
                        </span>
                        <h3 class="flex items-center mb-1 text-lg font-semibold text-gray-900 dark:text-white">
                            {decision.name}
                        </h3>
                        <time class="block mb-2 text-sm font-normal leading-none text-gray-400 dark:text-gray-500">
                            Date: {new Date(decision.date).toLocaleDateString()}
                        </time>
                        <p class="mb-4 text-base font-normal text-gray-500 dark:text-gray-400">
                            {decision.description}
                        </p>
                        <Button class="text-sm" on:click={() => goto(`/decision/edit/${decision.id}`)}>
                            View Details
                        </Button>
                    </li>
                {/each}
            </ol>
        {:else}
            <p class="text-gray-500 dark:text-gray-400">No decisions made for this case yet.</p>
        {/if}
    </div>


{/if}