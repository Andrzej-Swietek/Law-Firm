import apiClient from "$lib/services/apiClient";
import type {Case} from "$lib/interfaces/case.interface";
import type {ErrorResponse} from "$lib/interfaces/response.interface";
import {toast} from "svelte-sonner";

type CaseDTO = Partial<Case>

export async function createCase(payload: CaseDTO): Promise<Case|ErrorResponse|null> {
    try {
        const result =  await apiClient.post<Case|ErrorResponse>(`/api/v1/cases`, payload);
        if ('errors' in result) {
            const errorResponse = result as ErrorResponse;

            Object.entries(errorResponse.errors).forEach(([field, message]) => {
                toast.error(`${field}: ${message}`);
            });

            return errorResponse;
        }
        return result;
    } catch (e: any) {
        console.log(e.data)
        if (e.data && e.data.errors) {
            const errorResponse = e.data as ErrorResponse;
            return errorResponse;
        }
        return null
    }
}