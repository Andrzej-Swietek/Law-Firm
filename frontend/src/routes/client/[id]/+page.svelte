<script lang="ts">
    import {onMount} from "svelte";
    import { toast } from "svelte-sonner";
    import {goto} from "$app/navigation";
    import { page } from '$app/stores';

    import {Button} from "$lib/components/ui/button";
    import PageHeader from "@components/table/PageHeader.svelte";
    import * as Table from "$lib/components/ui/table/index";
    import * as Pagination from "$lib/components/ui/pagination";
    import * as Card from "$lib/components/ui/card";
    import * as Dialog from "$lib/components/ui/dialog";

    import LoadingSpinner from "@components/common/LoadingSpinner.svelte";

    import type {Client, Lawyer} from "$lib/interfaces/person.interface";
    import type {Case} from "$lib/interfaces/case.interface";
    import type {Trial} from "$lib/interfaces/trial.interface";
    import type {RequiredDocumentForTrial} from "$lib/interfaces/document.interface";

    import {getClientById} from "$lib/api/client/getClientById";
    import {getLawyerById} from "$lib/api/lawyer/getLawyerById";
    import {getCasesByClient} from "$lib/api/case/getCasesByClient";
    import {getTrialByClientId} from "$lib/api/trial/getTrialById";
    import { getRequiredDocumentsByTrialId } from "$lib/api/document/requiredDocument/getRequiredDocumentsByTrialId";



    let client = $state<Client|null>(null);
    let cases = $state<Case[]>([])
    let trials = $state<Trial[]>()
    let requiredDocuments = $state<Record<number, RequiredDocumentForTrial[]>>({})

    onMount(async()=>{
        const [
            clientData,
            casesData,
            trialsData
        ] = await Promise.all([
            getClientById($page.params.id),
            getCasesByClient($page.params.id),
            getTrialByClientId($page.params.id)
        ]);
        if (clientData) client = {...clientData}
        else goto(`/client`)

        cases= [...casesData];
        trials = [...trialsData];

        for (const trial of trials) {
            const docs = await getRequiredDocumentsByTrialId(trial.id);
            requiredDocuments[trial.id] = [...docs];
        }

    })
</script>

<PageHeader
        title="Client Management"
        description="Manage Clients - Details"
        onCreate={ ()=> goto("/client/create") }
/>

{#if client}
    <div class="max-w-4xl px-8 flex flex-row gap-8 mt-8 mb-8">
        <h1 class="text-2xl font-bold mb-16 w-full flex-1 ">Client ID: {client.id}</h1>
    </div>

    <div class="flex flex-row flex-wrap gap-8 mb-16">
        <Card.Root class="w-full px-16 py-8">
            <Card.Header>
                <Card.Title class="font-black text-2xl">{ client.firstName } {client.lastName}</Card.Title>
                <Card.Description class="text-xl mt-4"> {client.email}  </Card.Description>
            </Card.Header>
            <Card.Content>
                <Card.Description class="text-xl mt-4"> Address:  </Card.Description>
                <p class="my-2"> <span class="ml-2 font-black">Street: { client.contactDetails?.street}</span> </p>
                <p class="my-2"> <span class="ml-2 font-black">City: { client.contactDetails?.city || '-'}</span> </p>
                <p class="my-2"> <span class="ml-2 font-black">ZipCode: { client.contactDetails?.zipCode }</span> </p>
                <p class="my-2"> <span class="ml-2 font-black">State: { client.contactDetails?.state }</span> </p>
                <p class="my-2"> <span class="ml-2 font-black">Country: { client.contactDetails?.country }</span> </p>
            </Card.Content>
        </Card.Root>
    </div>

    <div>
        <h2 class="text-xl font-bold my-4">Cases</h2>
        {#if cases && cases.length > 0}
            {#each cases as caseItem}
                <Card.Root class="w-full px-16 py-8 mb-8">
                    <Card.Header>
                        <Card.Title class="font-black text-xl">Case ID: {caseItem.id}</Card.Title>
                        <Card.Description class="font-black text-xl"> {caseItem.description}</Card.Description>
                    </Card.Header>
                    <Card.Content>
                        <p class="my-2">Lawyer: {caseItem.responsibleLawyer?.firstName} {caseItem.responsibleLawyer?.lastName}</p>
                        <p class="my-2">Specialization: {caseItem.responsibleLawyer?.specialization}</p>
                    </Card.Content>
                </Card.Root>
            {/each}
            {:else}
            <div>
                <Card.Root class="w-full px-16 py-8 mb-8">
                    No cases Found
                </Card.Root>
            </div>
        {/if}
    </div>
    <div>
        <h2 class="text-xl font-bold my-4">Trials</h2>
        {#if trials}
            {#each trials as trial}
                <Card.Root class="w-full px-16 py-8 mb-8">
                    <Card.Header>
                        <Card.Title class="font-black text-xl">Trial ID: {trial.id}</Card.Title>
                    </Card.Header>
                    <Card.Content>
                        <p class="my-2">Status: {trial.trialStatus?.name}</p>
                        <p class="my-2">Date: {trial.date || '-'}</p>
                        <p class="my-2">Judge: {trial.judge?.firstName } {trial.judge?.lastName }</p>
                        <p class="my-2">Lawyer: {trial.lawyer?.firstName } {trial.lawyer?.lastName } - {trial.lawyer?.specialization}</p>
                        <p class="my-2">Court: {trial.judge?.courtDivision?.name } </p>
                        <h3 class="text-lg font-bold mt-4">Required Documents:</h3>
                        <ul>
                            {#if requiredDocuments[trial.id]?.length}
                                {#each requiredDocuments[trial.id] as doc}
                                    <li>{doc.document?.title} - {doc.trial?.title}</li>
                                {/each}
                            {:else}
                                <li>No documents required</li>
                            {/if}
                        </ul>
                    </Card.Content>
                    <Card.Footer>
                        <Button on:click={()=>goto(`/trial/${trial.id}`)}>Trial Details</Button>
                    </Card.Footer>
                </Card.Root>
            {/each}
        {/if}
    </div>

    {:else}
    <div class="w-full flex-center !min-h-[50vh] my-16">
        <LoadingSpinner width="150px" height="150px" color="rgb(55 65 81/.7)" />
    </div>
{/if}