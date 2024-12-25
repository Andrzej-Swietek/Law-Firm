<script lang="ts">
    import { onMount } from "svelte";
    import { toast } from "svelte-sonner";
    import { createRuling } from "$lib/api/ruling/createRuling";
    import { getAllTrials } from "$lib/api/trial/getAllTrials";
    import { goto } from "$app/navigation";

    import { Input } from "$lib/components/ui/input";
    import { Label } from "$lib/components/ui/label";
    import { Button } from "$lib/components/ui/button";
    import { Select, Trigger, Value, Content, Item } from "$lib/components/ui/select/index";
    import { Textarea } from "$lib/components/ui/textarea/index";
    import { Checkbox } from "$lib/components/ui/checkbox/index";

    import CalendarIcon from "lucide-svelte/icons/calendar";
    import {
        DateFormatter,
        type DateValue,
        getLocalTimeZone,
    } from "@internationalized/date";
    import { cn } from "$lib/utils";
    import { Calendar } from "$lib/components/ui/calendar/index";
    import * as Popover from "$lib/components/ui/popover/index";

    import type { Ruling } from "$lib/interfaces/ruling.interface";
    import type {Trial} from "$lib/interfaces/trial.interface";

    let ruling: Omit<Ruling, 'id' | 'trial'> = {
        isFinal: false,
        content: "",
        trialId: 0,
        finalizationDate: "",
    };

    let trials: Trial[] = [];
    let selectedTrialId: number | null = null;

    let dateValue: DateValue | undefined = undefined;
    const df = new DateFormatter("en-US", { dateStyle: "long" });

    onMount(async () => {
        trials = await getAllTrials();
    });

    const handleSubmit = async () => {
        if (!selectedTrialId || !dateValue || !ruling.content.trim()) {
            toast.error("Please fill out all required fields.");
            return;
        }
        ruling.finalizationDate = `${dateValue!.toDate(getLocalTimeZone()).toISOString().split("T")[0]}`;

        try {
            const newRuling = await createRuling({
                ...ruling,
                trialId: selectedTrialId,
            });

            if (newRuling) {
                toast.success("Ruling successfully created!");
                goto("/ruling");
                return;
            }

            toast.error("Failed to create ruling. Please try again.");
        } catch (error) {
            console.error("Error creating ruling:", error);
            toast.error("Failed to create ruling. Please try again.");
        }
    };
</script>

<div class="max-w-4xl mx-auto p-8">
    <h1 class="text-2xl font-bold mb-16">Create New Ruling</h1>
    <form
            class="space-y-4"
            on:submit|preventDefault={handleSubmit}
    >
        <!-- Trial -->
        <div>
            <Label for="trial">Trial</Label>
            <Select
                    selected={trials.find(trial => trial.id === selectedTrialId)}
                    onSelectedChange={(selected) => {
                    selected && (selectedTrialId = Number(selected.value));
                }}
            >
                <Trigger>
                    <Value placeholder="Select a trial" />
                </Trigger>
                <Content>
                    {#each trials as trial}
                        <Item value={trial.id} label={`Trial ${trial.id}`} />
                    {/each}
                </Content>
            </Select>
        </div>

        <!-- Content -->
        <div>
            <Label for="content">Content</Label>
            <Textarea
                    id="content"
                    placeholder="Type your ruling content here."
                    bind:value={ruling.content}
                    required
            />
        </div>

        <!-- Finalization Date -->
<!--        <div>-->
<!--            <Label for="finalizationDate">Finalization Date</Label>-->
<!--            <Input-->
<!--                    id="finalizationDate"-->
<!--                    type="date"-->
<!--                    bind:value={ruling.finalizationDate}-->
<!--                    required-->
<!--            />-->
<!--        </div>-->

        <div class="flex items-center gap-4">
            <Label for="finalizationDate">Finalization Date</Label>
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


        <!-- Is Final -->
        <div>
            <Label for="isFinal">Is Final</Label>
            <Checkbox class="mx-4" id="isFinal" bind:checked={ruling.isFinal} aria-labelledby="terms-label" />
        </div>

        <!-- Submit Button -->
        <Button type="submit" class="w-full mt-6">
            Create Ruling
        </Button>
    </form>
</div>
