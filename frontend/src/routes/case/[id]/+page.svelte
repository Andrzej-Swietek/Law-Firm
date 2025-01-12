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

    import type {Client, Lawyer} from "$lib/interfaces/person.interface";
    import type {Case} from "$lib/interfaces/case.interface";
    import type {Trial} from "$lib/interfaces/trial.interface";
    import type {RequiredDocumentForTrial} from "$lib/interfaces/document.interface";
    import type {Decision} from "$lib/interfaces/decision.interface";

    import {getTrialByClientId, getTrialsByCaseId} from "$lib/api/trial/getTrialById";
    import {getCaseById} from "$lib/api/case/getCaseById";
    import {getRequiredDocumentsByCaseId} from "$lib/api/document/requiredDocument/getRequiredDocumentsByCaseId";
    import {getSignaturesByRequiredDocument} from "$lib/api/signature/getSignaturesByRequiredDocument";
    import {getDecisionsByCaseId} from "$lib/api/decision/getDecisionById";
    import type {Ruling} from "$lib/interfaces/ruling.interface";
    import {getRulingsByCaseId} from "$lib/api/ruling/getRullingById";


    let caseDetails = $state<Case|null>(null);
    let trials = $state<Trial[]>()
    let requiredDocuments = $state<RequiredDocumentForTrial[]>([]);
    let decisions = $state<Decision[]>([])
    let rulings = $state<Ruling[]>([])

    async function loadSignatures() {
        await Promise.all(
            requiredDocuments.map(async (doc) => {
                const signatures = await getSignaturesByRequiredDocument(doc.id);
                doc.signatures = [...signatures];
            })
        );
    }

    const capitalize = (word:string) => word.charAt(0).toUpperCase() + word.slice(1);

    onMount(async()=> {
        const [
            caseData,
            trialsData,
            requiredDocumentsData,
            decisionsData,
            rulingData
        ] = await Promise.all([
            getCaseById($page.params.id),
            getTrialsByCaseId($page.params.id),
            getRequiredDocumentsByCaseId($page.params.id),
            getDecisionsByCaseId($page.params.id),
            getRulingsByCaseId($page.params.id)
        ]);
        if (caseData) caseDetails = {...caseData}
        else goto(`/case`)

        trials = [...trialsData];
        requiredDocuments = [...requiredDocumentsData];
        decisions = [...decisionsData];
        rulings = [...rulingData];
        loadSignatures()
    });

</script>


<PageHeader
        title="Case Management"
        description="Manage Case - Details"
        onCreate={ ()=> goto("/case/create") }
/>

{#if caseDetails}
    <div class="max-w-4xl px-8 flex flex-row gap-8 mt-8 mb-8">
        <h1 class="text-2xl font-bold mb-16 w-full flex-1 ">Case ID: {caseDetails.id}</h1>
    </div>

    <div class="flex flex-row flex-wrap gap-8 mb-16">
        <Card.Root class="w-full px-16 py-8">
            <Card.Header>
                <Card.Title class="font-black text-2xl">{ caseDetails.name }</Card.Title>
                <Card.Description class="text-xl mt-4"> {caseDetails.description}  </Card.Description>
            </Card.Header>
            <Card.Content>
                <Card.Description class="text-xl mt-4"> Involved  </Card.Description>
                <p class="my-2">Client: <span class="ml-2 font-black"> { caseDetails.client?.firstName} { caseDetails.client?.lastName}</span> </p>
                <p class="my-2">Lawyer:  <span class="ml-2 font-black">{ caseDetails.responsibleLawyer?.firstName} { caseDetails.responsibleLawyer?.lastName}</span> </p>
            </Card.Content>
            <Card.Footer class="flex flex-row gap-4">
                <Button on:click={()=> goto(`/client/${caseDetails?.clientId}`)}>Client Details</Button>
                <Button on:click={()=> goto(`/client/${caseDetails?.responsibleLawyerId}`)}>Lawyer Details</Button>
            </Card.Footer>
        </Card.Root>
    </div>

    <div class="w-full mt-16 px-8">
        <h2 class="text-2xl font-bold mb-8">Trial Timeline</h2>
        {#if trials && trials.length > 0}
            <ol class="relative border-l border-gray-200 dark:border-gray-700">
                {#each trials as trial, i}
                    <li class="mb-10 ml-6">
                    <span class="absolute flex items-center justify-center w-8 h-8 bg-blue-100 rounded-full -left-4 ring-8 ring-white dark:ring-gray-900 dark:bg-blue-900">
                        {i + 1}
                    </span>
                        <h3 class="flex items-center mb-1 text-lg font-semibold text-gray-900 dark:text-white">
                            {trial.title}
                            {#if trial.trialStatus?.name === 'FINISHED'}
                                <span class="bg-green-100 text-green-800 text-sm font-medium mr-2 px-2.5 py-0.5 rounded dark:bg-green-900 dark:text-green-300 ml-3">
                                  { trial.trialStatus?.name}
                                </span>
                            {:else}
                                <span class="bg-red-100 text-red-800 text-sm font-medium mr-2 px-2.5 py-0.5 rounded dark:bg-red-900 dark:text-red-300 ml-3">
                                     { trial.trialStatus?.name}
                                </span>
                            {/if}
                        </h3>
                        <time class="block mb-2 text-sm font-normal leading-none text-gray-400 dark:text-gray-500">
                            Scheduled: {new Date(trial.date).toLocaleDateString()}
                        </time>
                        <p class="mb-4 text-base font-normal text-gray-500 dark:text-gray-400">
                            {trial.description}
                        </p>
                        <Button class="text-sm" on:click={() => goto(`/trial/${trial.id}`)}>
                            View Details
                        </Button>
                    </li>
                {/each}
            </ol>
        {:else}
            <p class="text-gray-500 dark:text-gray-400">No trials scheduled for this case.</p>
        {/if}
    </div>

    <div class="w-full mt-16 px-8">
        <h2 class="text-2xl font-bold mb-8">Decisions Timeline</h2>

        {#if decisions && decisions.length > 0}
            <ol class="relative border-l border-gray-200 dark:border-gray-700">
                {#each decisions as decision, i}
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

    <div class="w-full mt-16 px-8">
        <h2 class="text-2xl font-bold mb-8">Ruling Timeline</h2>

        {#if rulings && rulings.length > 0}
            <ol class="relative border-l border-gray-200 dark:border-gray-700">
                {#each rulings as ruling, i}
                    <li class="mb-10 ml-6">
                        <span
                                class="absolute flex items-center justify-center w-8 h-8
                                   {ruling.isFinal ? 'bg-green-100' : 'bg-blue-100'}
                                   rounded-full -left-4 ring-8 ring-white dark:ring-gray-900
                                   {ruling.isFinal ? 'dark:bg-green-900' : 'dark:bg-blue-900'}">
                            {i + 1}
                        </span>
                        <h3 class="flex items-center mb-1 text-lg font-semibold text-gray-900 dark:text-white">
                            {ruling.isFinal ? 'Final Ruling' : 'Interim Ruling'}
                        </h3>
                        <time class="block mb-2 text-sm font-normal leading-none text-gray-400 dark:text-gray-500">
                            Finalization Date: {new Date(ruling.finalizationDate).toLocaleDateString()}
                        </time>
                        <p class="mb-4 text-base font-normal text-gray-500 dark:text-gray-400">
                            {ruling.content}
                        </p>
                        <Button class="text-sm" on:click={() => goto(`/trial/${ruling.trialId}`)}>
                            View Related Trial
                        </Button>
                    </li>
                {/each}
            </ol>
        {:else}
            <p class="text-gray-500 dark:text-gray-400">No rulings available for this case.</p>
        {/if}
    </div>

    <div class="w-full mt-16 px-8">
        <h2 class="text-2xl font-bold mb-8">Required Documents</h2>

        {#if requiredDocuments && requiredDocuments.length > 0}
            <ul class="divide-y divide-gray-200 dark:divide-gray-700">
                {#each requiredDocuments as document}
                    <li class="py-6 flex items-start justify-between">
                        <div>
                            <h3 class="text-lg font-medium text-gray-900 dark:text-white">{document.document?.title}</h3>
                            <p class="text-gray-500 dark:text-gray-400 text-sm mt-2">{document.document?.description}</p>
                            <p class="text-gray-500 dark:text-gray-400 text-sm mt-2">{document.document?.documentType?.name}</p>
                        </div>
                        <div>
                            <h3 class="text-lg font-medium text-gray-900 dark:text-white">Trial: {document.trial?.title}</h3>
                            <p class="text-gray-500 dark:text-gray-400 text-sm mt-2">{document.trial?.description}</p>
                        </div>
                        <div>
                            <h3 class="text-lg font-medium text-gray-900 dark:text-white">Signatures</h3>
                            {#if document.signatures && document.signatures.length > 0}
                                <ul>
                                    {#each document.signatures as signature}
                                        <li class="text-sm text-gray-700 dark:text-gray-300 mt-2">
                                            <strong>{capitalize(signature.role)}:</strong> {signature.person?.firstName} {signature.person?.lastName} <br />
                                            <strong>Date:</strong> {signature.date ? new Date(signature.date).toLocaleDateString() : 'Pending'}
                                        </li>
                                    {/each}
                                </ul>
                            {:else}
                                <p class="text-gray-500 dark:text-gray-400 text-sm mt-2">No signatures yet.</p>
                            {/if}
                        </div>
                    </li>
                {/each}
            </ul>
        {:else}
            <p class="text-gray-500 dark:text-gray-400">No Required Documents for this case.</p>
        {/if}
    </div>

{:else}
    <div class="w-full flex-center !min-h-[50vh] my-16">
        <LoadingSpinner width="150px" height="150px" color="rgb(55 65 81/.7)" />
    </div>
{/if}