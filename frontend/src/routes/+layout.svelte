<script lang="ts">
	import '../app.css';
    import ScrollUpBtn from "../components/common/ScrollUpBtn.svelte";
    import Footer from "../components/common/Footer.svelte";
    import Sidebar from "@components/common/Sidebar.svelte";
    import { navigating } from '$app/stores'

    import * as Breadcrumb from "$lib/components/ui/breadcrumb/index";
    import { Toaster } from "$lib/components/ui/sonner";

    import {onMount} from "svelte";

    let { children } = $props();

    let breadcrumbs: { name: string; href: string }[] = $state([]);

    $effect.pre(()=>{
        if ($navigating) {}
            const path = window.location.pathname;
            const segments = path.split('/').filter(Boolean);

            breadcrumbs = segments.map((segment, index) => ({
                name: segment.charAt(0).toUpperCase() + segment.slice(1),
                href: '/' + segments.slice(0, index + 1).join('/'),
            }));

    })

</script>

<div class="grid grid-cols-12 min-h-screen">
    <Sidebar />
    <div class="col-span-9 flex flex-col">
        <div class="px-8 py-8">
            <Breadcrumb.Root>
                <Breadcrumb.List>
                    <Breadcrumb.Item>
                        <Breadcrumb.Link href="/">Home</Breadcrumb.Link>
                    </Breadcrumb.Item>
                    {#each breadcrumbs as breadcrumb, i}
                        <Breadcrumb.Separator />
                        <Breadcrumb.Item>
                            {#if i < breadcrumbs.length - 1}
                                <Breadcrumb.Link href={breadcrumb.href}>{breadcrumb.name}</Breadcrumb.Link>
                            {:else}
                                <Breadcrumb.Page>{breadcrumb.name}</Breadcrumb.Page>
                            {/if}
                        </Breadcrumb.Item>
                    {/each}
                </Breadcrumb.List>
            </Breadcrumb.Root>
        </div>
        <main data-subpage-start class="flex-1 p-8">
            {@render children()}
        </main>
        <Footer />
    </div>
    <ScrollUpBtn />
    <Toaster />
</div>