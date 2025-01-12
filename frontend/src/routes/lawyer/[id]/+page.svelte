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

    import {getCasesByLawyer} from "$lib/api/case/getCasesByLawyer";
    import {getTrialByLawyerId} from "$lib/api/trial/getTrialById.js";
    import {getLawyerById} from "$lib/api/lawyer/getLawyerById";
    import {getRequiredDocumentsByTrialId} from "$lib/api/document/requiredDocument/getRequiredDocumentsByTrialId";
    import {getClientByLawyerId} from "$lib/api/client/getClientById";


    let lawyer = $state<Lawyer>();
    let clients = $state<Client[]>([]);
    let cases = $state<Case[]>([]);
    let trials = $state<Trial[]>();
    let requiredDocuments = $state<Record<number, RequiredDocumentForTrial[]>>({})

    onMount(async()=> {
        const [
            lawyerData,
            casesData,
            trialsData,
            clientsData
        ] = await Promise.all([
            getLawyerById($page.params.id),
            getCasesByLawyer($page.params.id),
            getTrialByLawyerId($page.params.id),
            getClientByLawyerId($page.params.id)
        ]);

        if (lawyerData) lawyer = {...lawyerData}
        else goto(`/lawyer`)
        cases= [...casesData];
        trials = [...trialsData];
        clients = [...clientsData];

        for (const trial of trials) {
            const docs = await getRequiredDocumentsByTrialId(trial.id);
            requiredDocuments[trial.id] = [...docs];
        }
    })
</script>

<PageHeader
        title="Lawyers Management"
        description="Manage Lawyers - Details"
        onCreate={ ()=> goto("/lawyer/create") }
/>

{#if lawyer}
    <div class="max-w-4xl px-8 flex flex-row gap-8 mt-8 mb-8">
        <h1 class="text-2xl font-bold mb-16 w-full flex-1 ">Lawyer ID: {lawyer.id}</h1>
    </div>

    <Card.Root class="w-full px-16 py-8">
        <Card.Header>
            <Card.Title class="font-black text-2xl">{ lawyer.firstName } {lawyer.lastName}</Card.Title>
            <Card.Description class="text-xl mt-4"> {lawyer.specialization}  </Card.Description>
        </Card.Header>
        <Card.Content>
            <p class="my-2"> <span class="ml-2 font-black">Pending Cases: { cases?.length }</span> </p>
            <p class="my-2"> <span class="ml-2 font-black">Total Trials: { trials?.length }</span> </p>
            <p class="my-2"> <span class="ml-2 font-black">Total Clients: { clients?.length }</span> </p>
        </Card.Content>
    </Card.Root>

    <div>
        <h2 class="text-xl font-bold my-4">Clients</h2>
        {#if clients && clients.length > 0}
            {#each clients as client}
                <Card.Root class="w-full px-16 py-8 mb-8 grid grid-cols-2">
                    <Card.Header>
                        <Card.Title class="font-black text-xl">Client ID: {client.id}</Card.Title>
                        <Card.Description class="font-black text-xl"> {client.firstName} {client.lastName}</Card.Description>
                    </Card.Header>
                    <Card.Content>
                        <p class="my-2">Email: {client.email}</p>
                        <p class="my-2">Phone: {client.contactDetails?.phoneNumber}</p>
                    </Card.Content>
                    <Card.Footer class="col-span-full">
                        <Button on:click={()=>goto(`/client/${client.id}`)}>Client Details</Button>
                    </Card.Footer>
                </Card.Root>
            {/each}
            {:else}
            <div>
                <Card.Root class="w-full px-16 py-8 mb-8">
                    No clients Found
                </Card.Root>
            </div>
        {/if}
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
                        <p class="my-2">Client: {trial.client?.firstName } {trial.client?.lastName }</p>
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