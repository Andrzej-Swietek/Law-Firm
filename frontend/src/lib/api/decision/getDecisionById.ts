import apiClient from "$lib/services/apiClient";
import type {Decision} from "$lib/interfaces/decision.interface";

export async function getDecisionById(id: string): Promise<Decision|null> {
    try {
        return await apiClient.get<Decision>(`/api/v1/decisions/${id}`)
    } catch (e) {
        return null;
    }
}