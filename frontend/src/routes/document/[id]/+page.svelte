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

    import type { Document } from "$lib/interfaces/document.interface";
    import LoadingSpinner from "@components/common/LoadingSpinner.svelte";

    import {getDocumentById} from "$lib/api/document/getDocumentById";
    import {downloadFileFromStorage} from "$lib/api/storage/downloadFileFromStorage";
    import {getCasesForRequiredDocument} from "$lib/api/document/requiredDocument/getCasesForRequiredDocument";

    import type {Client, Lawyer} from "$lib/interfaces/person.interface";
    import type {Case} from "$lib/interfaces/case.interface";
    import {getClientById} from "$lib/api/client/getClientById";
    import {getLawyerById} from "$lib/api/lawyer/getLawyerById";

    let documentDetails = $state<Partial<Document>|null>(null)
    let cases = $state<Case[]>([]);

    let client = $state<Client|null>(null)
    let lawyer = $state<Lawyer|null>(null)

    const downloadFile = async() => {
        if (documentDetails?.filePath)
            await downloadFileFromStorage(documentDetails?.filePath)
    }

    const openClientModal = async(clientId: number) => {
        const response= await getClientById(clientId)
        if (response)
            client = {...response}
    }

    const openLawyerModal = async (lawyerId: number) => {
        const response= await getLawyerById(lawyerId);
        if (response)
            lawyer = {...response}
    }

    onMount(async()=>{
        const response = await getDocumentById($page.params.id);
        documentDetails = { id: $page.params.id,  ...response!.document! }
        if (response && response.document && response.document.id) {
            const casesData = await getCasesForRequiredDocument(response!.document!.id);
            cases = [...casesData]
        }
    })
</script>

<PageHeader
        title="Document Management"
        description="Manage Document - CRUD"
        onCreate={ ()=> goto("/document/create") }
/>
<Card.Root class="-mt-8 mb-16">
    <div class="p-6 flex items-center justify-between  rounded-lg shadow-sm">
        <div>
            <h1 class="text-2xl font-bold text-gray-800">Manage Document Types</h1>
            <p class="text-sm text-gray-600 mt-2">Each document has its type that helps organize and filter documents and files </p>
        </div>

        <div>
            <Button onclick={ ()=> goto("/document/document-type") } variant="outline"> View All </Button>
            <Button onclick={ ()=> goto("/document/document-type/create") } variant="outline"> Create New </Button>
        </div>
    </div>
</Card.Root>
{#if documentDetails}
    <div class="max-w-4xl px-8 flex flex-row gap-8 mt-8 mb-8">
        <h1 class="text-2xl font-bold mb-16 w-full flex-1 ">Document ID: {documentDetails.id}</h1>
    </div>
    <div class="flex flex-row flex-wrap gap-8 mb-16">
        <Card.Root class="w-full px-16 py-8">
            <Card.Header>
                <Card.Title class="font-black text-2xl">{ documentDetails.title }</Card.Title>
                <Card.Description class="text-xl mt-4"> {documentDetails.description}  </Card.Description>
            </Card.Header>
            <Card.Content>
                <p class="my-2">Type: <span class="ml-2 font-black">{ documentDetails.documentType?.name }</span> </p>
                <p class="my-2">File Path: <span class="ml-2 font-black">{ documentDetails.filePath }</span> </p>
            </Card.Content>
            <Card.Footer class="gap-4">
                <Button on:click={ ()=> downloadFile() }>Download</Button>
                <Button on:click={ () => goto(`/document/edit/${documentDetails?.id}`) }>Edit</Button>
            </Card.Footer>
        </Card.Root>

        <div class="w-full px-8 flex flex-row gap-8 mt-8 mb-8">
            <h2 class="text-2xl font-bold mb-16 w-full flex-1 ">All cases that require document</h2>
        </div>

        {#each cases as caseItem}
            <Card.Root class="w-full px-16 py-8 flex flex-row">
                <Card.Header>
                    <Card.Title class="font-black text-xl">{ caseItem.name }</Card.Title>
                    <Card.Description class="text-lg mt-4"> { caseItem.description }  </Card.Description>
                </Card.Header>
                <Card.Content>
                    <Dialog.Root>
                        <Dialog.Trigger>
                            <Button on:click={()=> openClientModal(caseItem.clientId)}>Client Details</Button>
                        </Dialog.Trigger>
                        <Dialog.Content class="py-12 px-8">
                            <Dialog.Header>
                                <Dialog.Title class="text-2xl">Client</Dialog.Title>
                                <Dialog.Description>
                                    {#if client}
                                        <div class="my-2 text-lg">Full Name: <b class="ml-2">{ client?.firstName } {client?.lastName}</b> </div>
                                        <div class="my-2 text-lg">E-mail: <b class="ml-2">{ client?.email }</b> </div>
                                        <div class="my-2 text-lg">Phone: <b class="ml-2">{ client?.contactDetails?.phoneNumber }</b> </div>
                                        <div class="my-2 text-lg">Street: <b class="ml-2">{ client?.contactDetails?.street }</b> </div>
                                        <div class="my-2 text-lg">City: <b class="ml-2">{ client?.contactDetails?.city }</b> </div>
                                        <div class="my-2 text-lg">Zip-Code: <b class="ml-2">{ client?.contactDetails?.zipCode }</b> </div>
                                        <div class="my-2 text-lg">State: <b class="ml-2">{ client?.contactDetails?.state }</b> </div>
                                        <div class="my-2 text-lg">Country: <b class="ml-2">{ client?.contactDetails?.country }</b> </div>
                                        {:else}
                                        <div class="w-full flex-center my-16">
                                            <LoadingSpinner width="150px" height="150px" color="rgb(55 65 81/.7)" />
                                        </div>
                                    {/if}

                                </Dialog.Description>
                            </Dialog.Header>
                        </Dialog.Content>
                    </Dialog.Root>

                    <Dialog.Root>
                        <Dialog.Trigger>
                            <Button on:click={()=> openLawyerModal(caseItem.responsibleLawyerId)}>Lawyer Details</Button>
                        </Dialog.Trigger>
                        <Dialog.Content>
                            <Dialog.Header>
                                <Dialog.Title class="text-2xl">Lawyer</Dialog.Title>
                                <Dialog.Description>
                                    {#if lawyer}
                                        <div class="my-2 text-lg">Full Name: <b class="ml-2">{ lawyer?.firstName } {lawyer?.lastName}</b> </div>
                                        <div class="my-2 text-lg">Specialization <b class="ml-2">{ lawyer?.specialization }</b> </div>
                                    {:else}
                                        <div class="w-full flex-center my-16">
                                            <LoadingSpinner width="150px" height="150px" color="rgb(55 65 81/.7)" />
                                        </div>
                                    {/if}
                                </Dialog.Description>
                            </Dialog.Header>
                        </Dialog.Content>
                    </Dialog.Root>
                </Card.Content>
            </Card.Root>
        {/each}
    </div>
    {:else}
    <div class="w-full flex-center !min-h-[50vh] my-16">
        <LoadingSpinner width="150px" height="150px" color="rgb(55 65 81/.7)" />
    </div>
{/if}