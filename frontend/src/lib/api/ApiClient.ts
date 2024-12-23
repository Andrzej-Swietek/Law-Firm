import { get } from 'svelte/store';
import { token } from '@root/stores/auth';


interface FetchOptions extends RequestInit {
    // Include query parameters for GET requests
    queryParams?: Record<string, string | number | boolean>;
}

class FetchApiClient {
    private readonly baseUrl: string;

    constructor(baseUrl: string) {
        this.baseUrl = baseUrl;
    }

    private buildUrl(endpoint: string, queryParams?: Record<string, string | number | boolean>): string {
        const url = new URL(endpoint, this.baseUrl);
        if (queryParams) {
            Object.entries(queryParams).forEach(([key, value]) => {
                url.searchParams.append(key, String(value));
            });
        }
        return url.toString();
    }

    private async request<T>(
        method: string,
        endpoint: string,
        options: FetchOptions = {}
    ): Promise<T> {
        const { queryParams, ...fetchOptions } = options;
        const url = this.buildUrl(endpoint, queryParams);

        const headers = new Headers(fetchOptions.headers || {});

        const authToken = get(token);
        if (authToken) {
            headers.set('Authorization', `Bearer ${authToken}`);
        }

        headers.set('Content-Type', 'application/json');

        const response = await fetch(url, {
            method,
            headers,
            ...fetchOptions,
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || `Error ${response.status}: ${response.statusText}`);
        }

        return response.headers.get('Content-Type')?.includes('application/json')
            ? response.json()
            : (undefined as T);
    }

    public get<T>(endpoint: string, options?: FetchOptions): Promise<T> {
        return this.request<T>('GET', endpoint, options);
    }

    public post<T>(endpoint: string, body?: unknown, options?: FetchOptions): Promise<T> {
        return this.request<T>('POST', endpoint, { ...options, body: JSON.stringify(body) });
    }

    public put<T>(endpoint: string, body?: unknown, options?: FetchOptions): Promise<T> {
        return this.request<T>('PUT', endpoint, { ...options, body: JSON.stringify(body) });
    }

    public delete<T>(endpoint: string, options?: FetchOptions): Promise<T> {
        return this.request<T>('DELETE', endpoint, options);
    }
}

export default FetchApiClient;