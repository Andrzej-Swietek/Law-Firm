import apiClient from "$lib/services/apiClient";
import type {Case} from "$lib/interfaces/case.interface";

type CaseDTO = Partial<Case>

export async function updateCase(id: number, payload: CaseDTO): Promise<Case|null> {
    try {
        const result: Case =  await apiClient.put<Case>(`/api/v1/cases/${id}`, payload);
        return result;
    } catch (e) {
        return null
    }
}