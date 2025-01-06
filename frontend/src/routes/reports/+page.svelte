<script lang="ts">
    import {goto} from "$app/navigation";
    import * as Resizable from "$lib/components/ui/resizable/index.js";
    import * as Table from "$lib/components/ui/table/index.js";
    import {Card, Header, Title, Content, Description} from '$lib/components/ui/card/index'
    import {Button} from "$lib/components/ui/button";
    import { Input } from "$lib/components/ui/input";


    import PageHeader from "@components/table/PageHeader.svelte";
    import {onMount} from "svelte";
    import {
        type ClientPayment,
        getAllClientsMonthlyPayment, getReportLawyerCases,
        getReportTopClients, getReportTrialDetails, type ReportLawyerCasesResponse,
        type TopClientsResponse, type TrialDetailsResponse
    } from "$lib/api/reports/report";
    import {Label} from "$lib/components/ui/label";

    let clientsPayments = $state<ClientPayment[]>([])
    let topClients = $state<TopClientsResponse|null>(null);
    let trialsDetails = $state<TrialDetailsResponse[]|null>(null);
    let reportLawyerCases = $state<ReportLawyerCasesResponse|null>(null)

    let minClientsCases = $state<number>(0);

    onMount(async()=>{
        const [
            clientPaymentsData,
            topClientsData,
            trialsDetailsData,
            reportLawyerCasesData
        ] = await Promise.all([
            getAllClientsMonthlyPayment(),
            getReportTopClients(),
            getReportTrialDetails(),
            getReportLawyerCases()
        ])

        clientsPayments = [...clientPaymentsData];
        topClients = {...topClientsData};
        trialsDetails = [...trialsDetailsData];
        reportLawyerCases = {...reportLawyerCasesData}
    })

    $effect(()=>{
        (async()=>{
            if (minClientsCases != null)
                reportLawyerCases = {...await getReportLawyerCases(minClientsCases)}
        })()
    })
</script>

<PageHeader
        title="Reports"
        description="General information about current company condition"
        onCreate={ ()=> {} }
/>

<div class="h-screen">
    <Resizable.PaneGroup direction="horizontal" class="w-full min-h-[100vh] rounded-lg border mb-32">
        <Resizable.Pane defaultSize={50} class="p-8">
            <div class="flex h-[100px] items-center justify-center ">
                <span class="font-semibold">Client Monthly Payments Summary</span>
            </div>
            <Table.Root>
                <Table.Header>
                    <Table.Row>
                        <Table.Head class="w-[100px]">ID</Table.Head>
                        <Table.Head>Client ID</Table.Head>
                        <Table.Head>Full Name</Table.Head>
                        <Table.Head>E-mail</Table.Head>
                        <Table.Head class="text-right">Amount</Table.Head>
                    </Table.Row>
                </Table.Header>
                <Table.Body>
                    {#each clientsPayments as clientPayment, i (i)}
                        <Table.Row>
                            <Table.Cell class="font-medium">{i}</Table.Cell>
                            <Table.Cell>{clientPayment.id}</Table.Cell>
                            <Table.Cell>{clientPayment.firstName} {clientPayment.lastName}</Table.Cell>
                            <Table.Cell>{clientPayment.email}</Table.Cell>
                            <Table.Cell class="text-right">{clientPayment.payment.toFixed(2)} zł</Table.Cell>
                        </Table.Row>
                    {/each}
                    <Table.Row>
                        <Table.Cell colspan={4} class="font-medium">Sum</Table.Cell>
                        <Table.Cell class="font-medium text-right">{ clientsPayments.reduce( (acc,v)=> acc + v.payment, 0.0 ).toFixed(2) } zł</Table.Cell>
                    </Table.Row>
                </Table.Body>
            </Table.Root>
        </Resizable.Pane>
        <Resizable.Handle withHandle />
        <Resizable.Pane defaultSize={50}>
            <Resizable.PaneGroup direction="vertical">
                <Resizable.Pane class="p-8" defaultSize={25}>

                    <Card>
                        <Header>
                            <Title class="w-full text-center pt-4">
                                Top Clients Count
                            </Title>
                        </Header>
                        <Content>
                            <Table.Root>
                                <Table.Header>
                                    <Table.Row>
                                        <Table.Head class="w-[100px]">ID</Table.Head>
                                        <Table.Head>Full Name</Table.Head>
                                        <Table.Head class="text-right">Trials Count</Table.Head>
                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    {#if topClients}
                                        {#each topClients.topClients as client, i (i)}
                                            <Table.Row>
                                                <Table.Cell class="font-medium">{i}</Table.Cell>
                                                <Table.Cell>{client.fullName}</Table.Cell>
                                                <Table.Cell class="text-right"> {client.trialCount} </Table.Cell>
                                            </Table.Row>
                                        {/each}
                                    {/if}
                                </Table.Body>
                            </Table.Root>
                        </Content>
                    </Card>

                </Resizable.Pane>
                <Resizable.Handle withHandle />
                <Resizable.Pane class="p-8" defaultSize={75}>

                    <Card>
                        <Header>
                            <Title class="w-full text-center pt-4">
                                Lawyer Cases
                            </Title>
                        </Header>
                        <Content>
                            <div class="py-2 flex flex-row gap-4">
                                <Label class="flex-1 flex-center"> Min. number of trials </Label>
                                <Input
                                    type="number"
                                    placeholder="min number of trials &nbsp;&nbsp; | &nbsp;&nbsp; Default &nbsp;:&nbsp; 0"
                                    class="w-full flex-1"
                                    bind:value={minClientsCases}
                                    min="0"
                                />
                                <Button on:click={()=> { minClientsCases = 0; }}> Reset </Button>
                            </div>

                            <Table.Root>
                                <Table.Header>
                                    <Table.Row>
                                        <Table.Head class="w-[100px]">ID</Table.Head>
                                        <Table.Head>Full Name</Table.Head>
                                        <Table.Head class="text-right">Trials Count</Table.Head>
                                    </Table.Row>
                                </Table.Header>
                                <Table.Body>
                                    {#if reportLawyerCases}
                                        {#each reportLawyerCases.lawyers as lawyer, i (i)}
                                            <Table.Row>
                                                <Table.Cell class="font-medium">{i}</Table.Cell>
                                                <Table.Cell>{ lawyer.fullName }</Table.Cell>
                                                <Table.Cell class="text-right"> { lawyer.trialCount } </Table.Cell>
                                            </Table.Row>
                                        {/each}
                                    {/if}
                                </Table.Body>
                            </Table.Root>
                        </Content>
                    </Card>

                </Resizable.Pane>
            </Resizable.PaneGroup>
        </Resizable.Pane>
    </Resizable.PaneGroup>
</div>
<Card class="p-8 mt-8">
    <Content>

        <Table.Root>
            <Table.Header>
                <Table.Row>
                    <Table.Head class="w-[100px]">ID</Table.Head>
                    <Table.Head>Trial Title</Table.Head>
                    <Table.Head>Trial Date</Table.Head>
                    <Table.Head>Trial Status</Table.Head>
                    <Table.Head>Required Documents Count</Table.Head>
                </Table.Row>
            </Table.Header>
            <Table.Body>
                {#if trialsDetails}
                    {#each trialsDetails as trial, i (i)}
                        <Table.Row>
                            <Table.Cell class="font-medium">{i}</Table.Cell>
                            <Table.Cell>{trial.trial_title}</Table.Cell>
                            <Table.Cell>{trial.trial_date}</Table.Cell>
                            <Table.Cell>{trial.status}</Table.Cell>
                            <Table.Cell>{trial.required_documents_count}</Table.Cell>
                        </Table.Row>
                    {/each}
                {/if}
            </Table.Body>
        </Table.Root>
    </Content>
</Card>
