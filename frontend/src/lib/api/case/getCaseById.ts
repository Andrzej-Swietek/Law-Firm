import type {Decision} from "$lib/interfaces/decision.interface";
import apiClient from "$lib/services/apiClient";
import type {Case} from "$lib/interfaces/case.interface";

export async function getCaseById(id: string): Promise<Case|null> {
    try {
        return await apiClient.get<Case>(`/api/v1/cases/${id}`)
    } catch (e) {
        return null;
    }
}