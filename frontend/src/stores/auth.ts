import { writable } from 'svelte/store';

export const token = writable<string | null>(null);

if (typeof window !== 'undefined') {
    const savedToken = localStorage.getItem('token');
    if (savedToken) {
        token.set(savedToken);
    }

    token.subscribe((value) => {
        if (value) {
            localStorage.setItem('token', value);
        } else {
            localStorage.removeItem('token');
        }
    });
}