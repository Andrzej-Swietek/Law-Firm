import apiClient from "$lib/services/apiClient";
import type {Case} from "$lib/interfaces/case.interface";

type CaseDTO = Partial<Case>

export async function createCase(payload: CaseDTO): Promise<Case|null> {
    try {
        const result =  await apiClient.post<Case>(`/api/v1/cases`, payload);
        return result;
    } catch (e) {
        return null
    }
}