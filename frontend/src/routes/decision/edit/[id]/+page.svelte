<script lang="ts">

    import {toast} from "svelte-sonner";
    import { goto } from "$app/navigation";

    import {Label} from "$lib/components/ui/label/index";
    import {Input} from "$lib/components/ui/input/index";

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
    import type {Decision} from "$lib/interfaces/decision.interface";
    import {createDecision} from "$lib/api/decision/createDecision";
    import type {Case} from "$lib/interfaces/case.interface";
    import {onMount} from "svelte";
    import {getAllCases} from "$lib/api/case/getAllCases";
    import {getDecisionById} from "$lib/api/decision/getDecisionById";

    import { page } from '$app/stores';
    import {updateDecision} from "$lib/api/decision/updateDecision";

    $: decisionId = $page.params.id;


    let dateValue: DateValue | undefined = undefined;
    const df = new DateFormatter("en-US", { dateStyle: "long" });

    let decision: Partial<Decision> = {
        name: "",
        description: "",
        date: "",
        caseID: -1
    }

    let selectedCaseId: number|null = null;

    let cases: Case[] = []

    const handleSubmit = async () => {
        try {
            if (
                !decision?.name?.trim() ||
                !decision?.description?.trim() ||
                !dateValue ||
                !selectedCaseId
            ) {
                toast.error("Please fill out all required fields.");
                return;
            }
            decision.date = `${dateValue!.toDate(getLocalTimeZone()).toISOString().split("T")[0]}`;

            const newDecision= await updateDecision(decisionId,{
                ...decision,
                id: decision.id,
                caseID: selectedCaseId,
            });
            if (newDecision) {
                toast.success("Decision successfully created!");
                goto("/decision");
                return;
            }
            toast.error("Failed to create decision. Please try again.");
        } catch (error) {
            console.error("Error creating decision:", error);
            toast.error("Failed to create decision. Please try again.");
        }
    };

    onMount(async () => {
        try {
            const [
                caseData,
                decisionData
            ] = await Promise.all([
                getAllCases(1, 10000),
                getDecisionById(decisionId)
            ]);

            cases = [...caseData]
            decision = {...decisionData};

        } catch (error) {
            console.error("Error fetching data:", error);
            toast.error("Failed to load data. Please try again.");
        }
    });

</script>

<div class="max-w-4xl mx-auto p-8">
    <h1 class="text-2xl font-bold mb-16">Create New Decision</h1>
    <form
            class="space-y-4"
            on:submit|preventDefault={() => handleSubmit()}
    >
        <div>
            <Label for="name">Decision Name</Label>
            <Input
                    id="name"
                    type="text"
                    placeholder="Enter name"
                    bind:value={decision.name}
                    required
            />
        </div>

        <div>
            <Label for="description">Description</Label>
            <Textarea
                    id="description"
                    placeholder="The decision content"
                    bind:value={decision.description}
                    required
            />
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
                    selected={cases.find((caseItem) => caseItem.id === decision.caseID)}
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
            Create Decision
        </Button>
    </form>
</div>