<script lang="ts">
    import { goto } from "$app/navigation";
    import { page } from '$app/stores';
    import type {Appeal} from "$lib/interfaces/appeal.interface";
    import {onMount} from "svelte";
    import {getAppealById} from "$lib/api/appeal/getAppealById";

    import PageHeader from "@components/table/PageHeader.svelte";
    import {Card, Header, Title, Content, Description} from '$lib/components/ui/card/index'
    import {Button} from "$lib/components/ui/button";

    let appealId = $page.params.id;

    let appeal = $state<Appeal|null>(null)

    onMount(async()=>{
        const appealData = await getAppealById(appealId)
        if (!appealData) goto("/appeal")
        appeal = { ...appealData! };
    })
</script>

<PageHeader
        title="Appeals Management"
        description="Manage appeals"
        onCreate={ ()=> goto("/appeal/create") }
/>
<div class="max-w-4xl flex flex-row gap-8 mb-16">
    <h1 class="text-2xl font-bold mb-16 w-full flex-1 ">Appeal ID: {appealId}</h1>
</div>
<div class="flex flex-row gap-8 mb-16">

    <Card class="flex-1">
        <Header>
            <Title class="mb-4"> Trial: { appeal?.trial?.title }</Title>
            <Description>
                {appeal?.trial?.description}
            </Description>
        </Header>
        <Content>
            <p> Date: {appeal?.trial?.date} </p>
        </Content>
    </Card>

    <Card class="flex-1">
        <Header>
            <Title class="mb-4"> Case: { appeal?.trial?.case?.name }</Title>
            <div class="py-2">
                Status:  <span class="font-bold text-sm px-4 mx-4 py-2 rounded-[10px] bg-sky-800 text-white"> { appeal?.trial?.trialStatus?.name } </span>
            </div>
            <Description>
                {appeal?.trial?.case?.description}
            </Description>
        </Header>
        <Content>
            <p> Date: {appeal?.trial?.date} </p>
        </Content>
    </Card>
</div>
<div class="flex flex-row gap-8">
    <Card class="flex-1">
        <Header>
            <Title class="mb-4"> Initial Ruling</Title>
            <Description>
                { appeal?.initialRuling?.content }
            </Description>
        </Header>
        <Content>
            <div class="py-2 mt-4">
                Finished:  <span class="font-bold text-sm px-4 mx-4 py-2 rounded-[10px] bg-sky-800 text-white"> { appeal?.initialRuling?.isFinal } </span>
            </div>
            <div class="py-2 mt-4">
                <Button on:click={()=> goto(`/ruling/${ appeal?.initialRuling?.id }`)}  type="submit" class="w-full mt-6">
                    See Ruling
                </Button>
            </div>
        </Content>
    </Card>

    <Card class="flex-1">
        <Header>
            <Title class="mb-4"> New Ruling ID: { appeal?.finalRulingId }</Title>
            <Description>
                { appeal?.finalRuling?.content }
            </Description>
        </Header>
        <Content>
            <div class="py-2 mt-4">
                Finished:  <span class="font-bold text-sm px-4 mx-4 py-2 rounded-[10px] bg-sky-800 text-white"> { appeal?.finalRuling?.isFinal } </span>
            </div>
            <div class="py-2 mt-4">
                <Button on:click={()=> goto(`/ruling/${ appeal?.finalRuling?.id }`)} type="submit" class="w-full mt-6">
                    See Ruling
                </Button>
            </div>
        </Content>
    </Card>
</div>

<div class="flex flex-row gap-8 mt-8">
    <Card class="flex-1">
        <Header>
            <Title class="mb-4"> Client </Title>
            <Description>
                First Name: { appeal?.trial?.client?.firstName }
            </Description>
            <Description>
                Last Name: { appeal?.trial?.client?.lastName }
            </Description>
        </Header>
        <Content>
            <p>Email:  { appeal?.trial?.client?.email }</p>
        </Content>
    </Card>

    <Card class="flex-1">
        <Header>
            <Title class="mb-4"> Lawyer: { appeal?.finalRulingId }</Title>
            <Description>
                First Name: { appeal?.trial?.lawyer?.firstName }
            </Description>
            <Description>
                Last Name: { appeal?.trial?.lawyer?.lastName }
            </Description>
        </Header>
        <Content>
            <p>Specialization: { appeal?.trial?.lawyer?.specialization }</p>
        </Content>
    </Card>
</div>
<div class="flex flex-row gap-8 mt-8">
    <Card class="flex-1 flex flex-row py-4">
        <Header  class="flex-1 h-full flex-center-col gap-4 py-4">
            <Title class="self-start px-4"> Judge </Title>
            <div class="w-full flex flex-col px-4">

                <Description>
                    First Name: { appeal?.trial?.judge?.firstName }
                </Description>
                <Description>
                    Last Name: { appeal?.trial?.judge?.lastName }
                </Description>
            </div>
            <Button on:click={()=> goto(`/ruling/edit/${ appeal?.finalRuling?.id }`)} type="submit" class="w-full mt-4">
                See Judge
            </Button>
        </Header>
        <Content class="flex-1 h-full flex-center-col border-2 rounded-[10px]">
            <p class="font-bold">Court: <span class="ml-8"> { appeal?.trial?.judge?.courtDivision?.name }</span></p>
            <p class="font-bold">Court City:  <span class="ml-8">{ appeal?.trial?.judge?.courtDivision?.city }</span></p>
        </Content>
    </Card>
</div>