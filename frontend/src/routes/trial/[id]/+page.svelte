<script lang="ts">
    import {goto} from "$app/navigation";
    import { page } from '$app/stores';
    import { onMount } from "svelte";
    import { toast } from "svelte-sonner";

    import PageHeader from "@components/table/PageHeader.svelte";
    import * as Table from "$lib/components/ui/table/index";
    import * as Pagination from "$lib/components/ui/pagination";
    import * as Card from "$lib/components/ui/card";
    import {Button} from "$lib/components/ui/button";

    import {getTrialById} from "$lib/api/trial/getTrialById";
    import {getLawyerById} from "$lib/api/lawyer/getLawyerById";

    import type {Trial} from "$lib/interfaces/trial.interface";
    import type {RequiredDocumentForTrial} from "$lib/interfaces/document.interface";
    import type {Signature} from "$lib/interfaces/signature.interface";

    import {getContactDetailsByCDId} from "$lib/api/client/contactDetails/getContactDetailsByCDId";
    import {getRequiredDocumentsByTrialId} from "$lib/api/document/requiredDocument/getRequiredDocumentsByTrialId";
    import {getSignatureByDocument} from "$lib/api/signature/getSignatureByDocument";
    import {getSignaturesByRequiredDocument} from "$lib/api/signature/getSignaturesByRequiredDocument";


    let trial = $state<Trial|null>(null)
    let requiredDocuments = $state<RequiredDocumentForTrial[]>([]);
    let documentSignatures = $state<Record<number, Signature[]>>({});

    const loadSignaturesForDocument = async(requiredDocumentId: number) => {
        try {
            const signatures = await getSignaturesByRequiredDocument(requiredDocumentId);
            documentSignatures = {
                ...documentSignatures,
                [requiredDocumentId]: signatures
            };
            toast.success(`Signatures loaded for document ${requiredDocumentId}`);
        } catch (error) {
            console.error(`Error loading signatures for document ${requiredDocumentId}:`, error);
            toast.error(`Failed to load signatures for document ${requiredDocumentId}`);
        }
    }

    onMount(async ()=>{
        const [trialData, requiredDocs] = await Promise.all([
            getTrialById($page.params.id),
            getRequiredDocumentsByTrialId($page.params.id)
        ]);

        if (trialData) {
            trial = {...trialData};

            if (trialData?.lawyer?.id != trialData?.case?.responsibleLawyerId) {
                const lawyerId = trialData!.case!.responsibleLawyerId;
                const responsibleLawyer = await getLawyerById(lawyerId);
                trial && trial.case && (trial.case.responsibleLawyer = { ...responsibleLawyer! })
            } else {
                trial && trial.case && (trial.case.responsibleLawyer = {...trialData.lawyer!})
            }

            const clientContactDetails = await getContactDetailsByCDId(trialData?.client?.contactDetailsId ?? 0)
            trial && trial.client && (trial.client.contactDetails = {...clientContactDetails!});
        }

        if( requiredDocs ) {
            requiredDocuments = [...requiredDocs]
        }

    })

</script>


<PageHeader
        title="Trial Management"
        description="Manage Trials - CRUD"
        onCreate={ ()=> goto("/trial/create") }
/>

<Card.Root class="w-full px-16 py-8">
    <Card.Header>
        <Card.Title class="font-black text-2xl">{trial?.title}</Card.Title>
        <Card.Description class="text-xl mt-4">{trial?.description}</Card.Description>
    </Card.Header>

    <Card.Content>
        <div class="mb-4">
            <p class="my-2">Status: <span class="ml-2 font-black">{trial?.trialStatus?.name}</span></p>
            <p class="my-2">Date: <span class="ml-2 font-black">{trial?.date}</span></p>
        </div>

        <hr class="my-4"/>

        <!-- Client Details -->
        <div class="mb-6">
            <h3 class="font-bold text-lg mb-2">Client Details</h3>
            <table class="table-auto w-full border-collapse border border-gray-300">
                <tbody>
                    <tr>
                        <td class="font-bold p-2 border border-gray-200">Name</td>
                        <td class="p-2 border border-gray-200">{trial?.client?.firstName} {trial?.client?.lastName}</td>
                    </tr>
                    <tr>
                        <td class="font-bold p-2 border border-gray-200">Email</td>
                        <td class="p-2 border border-gray-200">{trial?.client?.email}</td>
                    </tr>
                    <tr>
                        <td class="font-bold p-2 border border-gray-200">Phone</td>
                        <td class="p-2 border border-gray-200">{trial?.client?.contactDetails?.phoneNumber}</td>
                    </tr>
                    <tr>
                        <td class="font-bold p-2 border border-gray-200">Street</td>
                        <td class="p-2 border border-gray-200">{trial?.client?.contactDetails?.street}</td>
                    </tr>
                    <tr>
                        <td class="font-bold p-2 border border-gray-200">City</td>
                        <td class="p-2 border border-gray-200">{trial?.client?.contactDetails?.city}</td>
                    </tr>
                    <tr>
                        <td class="font-bold p-2 border border-gray-200">State</td>
                        <td class="p-2 border border-gray-200">{trial?.client?.contactDetails?.state}</td>
                    </tr>
                    <tr>
                        <td class="font-bold p-2 border border-gray-200">Zip Code</td>
                        <td class="p-2 border border-gray-200">{trial?.client?.contactDetails?.zipCode}</td>
                    </tr>
                </tbody>

            </table>
        </div>

        <hr class="my-4"/>

        <!-- Lawyer Details -->
        <div class="mb-6">
            <h3 class="font-bold text-lg mb-2">Lawyer Details</h3>
            <table class="table-auto w-full border-collapse border border-gray-300">
                <tbody>
                    <tr>
                        <td class="font-bold p-2 border border-gray-200">Name</td>
                        <td class="p-2 border border-gray-200">{trial?.lawyer?.firstName} {trial?.lawyer?.firstName}</td>
                    </tr>
                    <tr>
                        <td class="font-bold p-2 border border-gray-200">Specialization</td>
                        <td class="p-2 border border-gray-200">{trial?.lawyer?.specialization}</td>
                    </tr>
                </tbody>

            </table>
        </div>

        <!-- Judge Details -->
        <div class="mb-6">
            <h3 class="font-bold text-lg mb-2">Judge Details</h3>
            <table class="table-auto w-full border-collapse border border-gray-300">
                <tbody>
                <tr>
                    <td class="font-bold p-2 border border-gray-200">Name</td>
                    <td class="p-2 border border-gray-200">{trial?.judge?.firstName} {trial?.judge?.lastName}</td>
                </tr>
                <tr>
                    <td class="font-bold p-2 border border-gray-200">Court</td>
                    <td class="p-2 border border-gray-200">{trial?.judge?.courtDivision?.name} - {trial?.judge?.courtDivision?.city}</td>
                </tr>
                </tbody>
            </table>
        </div>

        <!-- Case Details -->
        <div class="mb-6">
            <h3 class="font-bold text-lg mb-2">Case Details</h3>
            <table class="table-auto w-full border-collapse border border-gray-300">
                <tbody>
                    <tr>
                        <td class="font-bold p-2 border border-gray-200">Name</td>
                        <td class="p-2 border border-gray-200">{trial?.case?.name}</td>
                    </tr>
                    <tr>
                        <td class="font-bold p-2 border border-gray-200">Description</td>
                        <td class="p-2 border border-gray-200">{trial?.case?.description}</td>
                    </tr>
                    <tr>
                        <td class="font-bold p-2 border border-gray-200">Responsible Lawyer</td>
                        <td class="p-2 border border-gray-200">
                            {trial?.case?.responsibleLawyer?.firstName} {trial?.case?.responsibleLawyer?.lastName}
                        </td>
                    </tr>
                </tbody>

            </table>
        </div>
    </Card.Content>
</Card.Root>

<Card.Root class="w-full px-16 py-8">
    <Card.Header>
        <Card.Title class="font-black text-2xl mt-4">Required Documents For this Trial</Card.Title>
        <Card.Description class="text-xl mt-4"></Card.Description>
    </Card.Header>
    <Card.Content>
        {#if requiredDocuments.length > 0}
            {#each requiredDocuments as requiredDocument, i (i)}
                <Card.Root class="w-full px-16 py-8">
                    <Card.Header class="flex flex-row items-center justify-between w-full">
                        <div>
                            <Card.Title class="font-bold text-2xl">RQID: {requiredDocument.id} | Document #{requiredDocument.document?.id}: <span class="mx-8 tracking-wide font-black text-slate-600">{requiredDocument.document?.title}</span></Card.Title>
                            <Card.Description class="text-xl mt-4">{requiredDocument?.document?.description}</Card.Description>
                        </div>
                        <div>
                            <Button on:click={()=> goto(`/document/${requiredDocument.document?.id}`)}>See Details</Button>
                            <Button on:click={()=> loadSignaturesForDocument(requiredDocument.id)}>Load Signatures</Button>
                        </div>

                    </Card.Header>
                    <hr class="my-4"/>
                    {#if documentSignatures[requiredDocument.id]}
                        <div>
                            <h4 class="font-bold text-lg mb-2">Signatures:</h4>
                            <ul>
                                {#each documentSignatures[requiredDocument.id] as signature}
                                    <li class="mb-2 w-full flex flex-row gap-4 items-center">
                                        <div>-</div>
                                        <div>{signature.date}</div>
                                        <div class="px-4 py-2 border rounded-[10px]">{signature.person?.firstName} {signature.person?.lastName}</div>
                                        <div class="px-4 py-2 bg-slate-600 text-white rounded-[10px]">{signature.role}</div>
                                    </li>
                                {/each}
                            </ul>
                        </div>
                    {:else}
                        <p class="text-gray-500 px-8">Signatures not loaded yet. Click "Load Signatures" to view them.</p>
                    {/if}
                </Card.Root>
            {/each}
            {:else}
            <p>
                No Required Documents for this trial
            </p>
        {/if}
    </Card.Content>
</Card.Root>