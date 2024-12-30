<script lang="ts">
    let user = {
        name: "User Example",
        email: "user@example.com",
        avatar: "https://images.unsplash.com/photo-1600486913747-55e5470d6f40?auto=format&fit=crop&w=1770&q=80"
    };
    type MenuOption = { name: string, label: string, subtabs?: MenuOption[] }
    const menuOptions: MenuOption[] = [
        {  name: 'client', label: 'Client Management'},
        {  name: 'appeal', label: 'Appeal Management'},
        {  name: 'case', label: 'Case Management'},
        {  name: 'court', label: 'Court Management'},
        {  name: 'decision', label: 'Decisions Management'},
        {
            name: 'document',
            label: 'Documents Management',
            subtabs: [
                {  name: 'document/document-type', label: 'Document Types' },
                {  name: 'document/required-documents', label: 'Required Documents' },
            ]
        },
        {  name: 'judge', label: 'Judges Management'},
        {  name: 'lawyer', label: 'Lawyers Management'},
        {  name: 'ruling', label: 'Ruling Management'},
        {  name: 'signature', label: 'Signatures Management'},
        {  name: 'trial', label: 'Trials Management'},
    ]
</script>

<div class="col-span-full sticky top-0 rounded-[10px] md:col-span-3 flex h-screen flex-col justify-between border bg-white">
    <div class="px-4 py-6">
        <span class="grid h-10 w-32 place-content-center rounded-lg bg-gray-100 text-xs text-gray-600">
            Logo
        </span>

        <ul class="mt-6 space-y-1">
            <li>
                <a href="/" class="block rounded-lg bg-gray-100 px-4 py-2 text-sm font-medium text-gray-700">
                    Dashboard
                </a>
            </li>

            {#each menuOptions as menuOption}
                <li>
                    <details class="group [&_summary::-webkit-details-marker]:hidden">
                        <summary
                                class="flex cursor-pointer items-center justify-between rounded-lg px-4 py-2 text-gray-500 hover:bg-gray-100 hover:text-gray-700">
                            <span class="text-sm font-medium"> { menuOption.label } </span>

                            <span class="shrink-0 transition duration-300 group-open:-rotate-180">
                            <svg xmlns="http://www.w3.org/2000/svg" class="size-5" viewBox="0 0 20 20"
                                 fill="currentColor">
                                <path fill-rule="evenodd"
                                      d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                                      clip-rule="evenodd" />
                            </svg>
                        </span>
                        </summary>

                        <ul class="mt-2 space-y-1 px-4">
                            <li>
                                <a href="/{menuOption.name}"
                                   class="block rounded-lg px-4 py-2 text-sm font-medium text-gray-500 hover:bg-gray-100 hover:text-gray-700">
                                    List { menuOption.name }
                                </a>
                            </li>

                            <li>
                                <a href="/{menuOption.name}/create"
                                   class="block rounded-lg px-4 py-2 text-sm font-medium text-gray-500 hover:bg-gray-100 hover:text-gray-700">
                                    Create { menuOption.name }
                                </a>
                            </li>

                            {#if menuOption.subtabs}
                                {#each menuOption?.subtabs as subtab}
                                    <li class="px-4">
                                        <details class="group/item [&_summary::-webkit-details-marker]:hidden">
                                            <summary
                                                    class="flex bg-slate-200 cursor-pointer items-center justify-between rounded-lg px-4 py-2 text-gray-500 hover:bg-gray-100 hover:text-gray-700">
                                                <span class="text-sm font-medium"> { subtab.label } </span>

                                                <span class="shrink-0 transition duration-300 group-open/item:-rotate-180">
                                                    <svg xmlns="http://www.w3.org/2000/svg" class="size-5" viewBox="0 0 20 20"
                                                         fill="currentColor">
                                                        <path fill-rule="evenodd"
                                                              d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
                                                              clip-rule="evenodd" />
                                                    </svg>
                                                </span>
                                            </summary>

                                            <ul class="mt-2 space-y-1 px-4">
                                                <li>
                                                    <a href="/{subtab.name}"
                                                       class="block rounded-lg px-4 py-2 text-sm font-medium text-gray-500 hover:bg-gray-100 hover:text-gray-700">
                                                        List { subtab.label }
                                                    </a>
                                                </li>

                                                <li>
                                                    <a href="/{subtab.name}/create"
                                                       class="block rounded-lg px-4 py-2 text-sm font-medium text-gray-500 hover:bg-gray-100 hover:text-gray-700">
                                                        Create { subtab.label }
                                                    </a>
                                                </li>
                                            </ul>
                                        </details>
                                    </li>
                                {/each}
                            {/if}
                        </ul>
                    </details>
                </li>
            {/each}

        </ul>
    </div>

    <div class="sticky inset-x-0 bottom-0 border-t border-gray-100 rounded-[11px] overflow-hidden">
        <a href="#" class="flex items-center gap-2 bg-white p-4 hover:bg-gray-50">
            <img alt=""
                 src="https://images.unsplash.com/photo-1600486913747-55e5470d6f40?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1770&q=80"
                 class="size-10 rounded-full object-cover" />

            <div>
                <p class="text-xs">
                    <strong class="block font-medium">User Example</strong>

                    <span> user@example.com </span>
                </p>
            </div>
        </a>
    </div>
</div>
