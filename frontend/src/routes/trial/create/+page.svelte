<script lang="ts">
    import { onMount } from "svelte";
    import { toast } from "svelte-sonner";
    import { goto } from "$app/navigation";

    import { createTrial } from "$lib/api/trial/createTrial";
    import { getAllClients } from "$lib/api/client/getAllClients";
    import { getAllLawyers } from "$lib/api/lawyer/getAllLawyers";
    import { getAllJudges } from "$lib/api/judge/getAllJudges";
    import { getAllCases } from "$lib/api/case/getAllCases";
    import {getAllTrialStatuses} from "$lib/api/trial/status/getAllTrialStatuses";

    import { Input } from "$lib/components/ui/input";
    import { Label } from "$lib/components/ui/label";
    import { Button } from "$lib/components/ui/button";
    import { Textarea } from "$lib/components/ui/textarea/index";
    import {
        Select,
        Trigger,
        Value,
        Content,
        Item,
    } from "$lib/components/ui/select/index";
    import CalendarIcon from "lucide-svelte/icons/calendar";
    import {
        DateFormatter,
        type DateValue,
        getLocalTimeZone,
    } from "@internationalized/date";
    import { cn } from "$lib/utils";

    import { Calendar } from "$lib/components/ui/calendar/index";
    import * as Popover from "$lib/components/ui/popover/index";


    import type {Trial, TrialStatus} from "$lib/interfaces/trial.interface";
    import type {Client, Judge, Lawyer} from "$lib/interfaces/person.interface";
    import type {Case} from "$lib/interfaces/case.interface";


    let trial: Omit<Trial, 'id'> = {
        title: "",
        description: "",
        trialStatusId: -1,
        clientId: -1,
        lawyerId: -1,
        judgeId: -1,
        date: "",
        caseId: -1,
    };

    let selectedStatusId: number|null = null;
    let selectedClientId: number|null = null;
    let selectedLawyerId: number|null = null;
    let selectedJudgeId: number|null = null;
    let selectedCaseId: number|null = null;

    let trialStatuses: TrialStatus[] = [];
    let clients: Client[] = [];
    let lawyers: Lawyer[] = [];
    let judges: Judge[] = [];
    let cases: Case[] = [];

    let dateValue: DateValue | undefined = undefined;
    const df = new DateFormatter("en-US", { dateStyle: "long" });

    onMount(async () => {
        try {
            const [
                trialStatusesData,
                clientsData,
                lawyersData,
                judgesData,
                casesData,
            ] = await Promise.all([
                getAllTrialStatuses(),
                getAllClients(1, 1000),
                getAllLawyers(1, 1000),
                getAllJudges(1, 1000),
                getAllCases(1, 1000),
            ]);

            trialStatuses = trialStatusesData;
            clients = clientsData;
            lawyers = lawyersData;
            judges = judgesData;
            cases = casesData;
        } catch (error) {
            console.error("Error fetching data:", error);
            toast.error("Failed to load data. Please try again.");
        }
    });

    const handleSubmit = async () => {
        if (
            !trial.title.trim() ||
            !trial.description.trim() ||
            !dateValue ||
            !trial.trialStatusId ||
            !selectedClientId ||
            !selectedLawyerId ||
            !selectedJudgeId ||
            !selectedCaseId ||
            !selectedStatusId
        ) {
            toast.error("Please fill out all required fields.");
            return;
        }

        try {
            trial.trialStatusId = selectedStatusId;
            trial.caseId = selectedCaseId;
            trial.judgeId = selectedJudgeId;
            trial.lawyerId = selectedLawyerId;
            trial.clientId = selectedClientId;
            trial.date = `${dateValue!.toDate(getLocalTimeZone()).toISOString().split("T")[0]}`;

            const newTrial = await createTrial(trial);
            if (newTrial) {
                toast.success("Trial successfully created!");
                goto("/trial");
                return;
            } else {
                toast.error("Failed to create trial. Please try again.");
            }
        } catch (error) {
            console.error("Error creating trial:", error);
            toast.error("Failed to create trial. Please try again.");
        }
    };
</script>

<div class="max-w-4xl mx-auto p-8">
    <h1 class="text-2xl font-bold mb-16">Create New Trial</h1>
    <form class="space-y-4" on:submit|preventDefault={handleSubmit}>
        <!-- Title -->
        <div>
            <Label for="title">Title</Label>
            <Input id="title" placeholder="Trial title" bind:value={trial.title} required />
        </div>

        <!-- Description -->
        <div>
            <Label for="description">Description</Label>
            <Textarea
                    id="description"
                    placeholder="Describe the trial"
                    bind:value={trial.description}
                    required
            />
        </div>

        <!-- Trial Status -->
        <div>
            <Label for="trialStatus">Trial Status</Label>
            <Select
                    selected={trialStatuses.find((status) => status.id === trial.trialStatusId)}
                    onSelectedChange={(selected) => selected && (selectedStatusId = Number(selected.value))}
            >
                <Trigger>
                    <Value placeholder="Select trial status" />
                </Trigger>
                <Content>
                    {#each trialStatuses as status}
                        <Item value={status.id} label={status.name} />
                    {/each}
                </Content>
            </Select>
        </div>

        <!-- Client -->
        <div>
            <Label for="client">Client</Label>
            <Select
                    selected={clients.find((client) => client.id === trial.clientId)}
                    onSelectedChange={(selected) => selected && (selectedClientId = Number(selected.value))}
            >
                <Trigger>
                    <Value placeholder="Select client" />
                </Trigger>
                <Content>
                    {#each clients as client}
                        <Item value={client.id} label={`${client.firstName} ${client.lastName}`} />
                    {/each}
                </Content>
            </Select>
        </div>

        <!-- Lawyer -->
        <div>
            <Label for="lawyer">Lawyer</Label>
            <Select
                    selected={lawyers.find((lawyer) => lawyer.id === trial.lawyerId)}
                    onSelectedChange={(selected) => selected && (selectedLawyerId = Number(selected.value))}
            >
                <Trigger>
                    <Value placeholder="Select lawyer" />
                </Trigger>
                <Content>
                    {#each lawyers as lawyer}
                        <Item value={lawyer.id} label={`${lawyer.firstName} ${lawyer.lastName}`} />
                    {/each}
                </Content>
            </Select>
        </div>

        <!-- Judge -->
        <div>
            <Label for="judge">Judge</Label>
            <Select
                    selected={judges.find((judge) => judge.id === trial.judgeId)}
                    onSelectedChange={(selected) => selected && (selectedJudgeId = Number(selected.value))}
            >
                <Trigger>
                    <Value placeholder="Select judge" />
                </Trigger>
                <Content>
                    {#each judges as judge}
                        <Item value={judge.id} label={`${judge.firstName} ${judge.lastName}`} />
                    {/each}
                </Content>
            </Select>
        </div>

        <!-- Date -->

        <div class="flex items-start justify-center gap-4 flex-col">
            <Label for="finalizationDate">Date</Label>
            <Popover.Root>
                <Popover.Trigger asChild let:builder>
                    <Button
                            variant="outline"
                            class={cn(
                            "w-[280px] justify-start text-left font-normal",
                            !dateValue && "text-muted-foreground"
                        )}
                            builders={[builder]}
                    >
                        <CalendarIcon class="mr-2 h-4 w-4" />
                        {dateValue ? df.format(dateValue.toDate(getLocalTimeZone())) : "Pick a date"}
                    </Button>
                </Popover.Trigger>
                <Popover.Content class="w-auto p-0">
                    <Calendar bind:value={dateValue} initialFocus />
                </Popover.Content>
            </Popover.Root>
        </div>

        <!-- Case -->
        <div>
            <Label for="case">Case</Label>
            <Select
                    selected={cases.find((caseItem) => caseItem.id === trial.caseId)}
                    onSelectedChange={(selected) => selected && (selectedCaseId = Number(selected.value))}
            >
                <Trigger>
                    <Value placeholder="Select case" />
                </Trigger>
                <Content>
                    {#each cases as caseItem}
                        <Item value={caseItem.id} label={caseItem.name} />
                    {/each}
                </Content>
            </Select>
        </div>

        <!-- Submit Button -->
        <Button type="submit" class="w-full mt-6">
            Create Trial
        </Button>
    </form>
</div>
