<script lang="ts">
    import {goto} from "$app/navigation";
    import * as Resizable from "$lib/components/ui/resizable/index.js";
    import {Card, Header, Title, Content, Description} from '$lib/components/ui/card/index'
    import {Button} from "$lib/components/ui/button";
</script>

<section>
    <Card>
        <Header>
            <Title> Reports </Title>
            <Description>
                Summary data for lawyers, clients and cases
            </Description>
        </Header>
        <Content>
            <Button on:click={()=> goto(`/reports`) }> More </Button>
        </Content>
    </Card>
</section>