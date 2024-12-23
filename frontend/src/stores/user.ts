import { writable } from 'svelte/store';

export const user = writable<any | null>(null);


if (typeof window !== 'undefined') {
    const savedUser = localStorage.getItem('user');

    if (savedUser) {
        user.set(JSON.parse(savedUser));
    }

    user.subscribe((value) => {
        if (value) {
            localStorage.setItem('user', JSON.stringify(value));
        } else {
            localStorage.removeItem('user');
        }
    });
}