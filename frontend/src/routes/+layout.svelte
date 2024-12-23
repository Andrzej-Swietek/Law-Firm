<script lang="ts">
	import '../app.css';
    import ScrollUpBtn from "../components/common/ScrollUpBtn.svelte";
    import Footer from "../components/common/Footer.svelte";
	let { children } = $props();
</script>

<div class="flex flex-col min-h-dvh bg-secondary">
    <main data-subpage-start class="flex-1 py-12">
        {@render children()}
    </main>

    <Footer />
    <ScrollUpBtn />
</div>