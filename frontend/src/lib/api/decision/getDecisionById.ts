import apiClient from "$lib/services/apiClient";
import type {Decision} from "$lib/interfaces/decision.interface";

export async function getDecisionById(id: string): Promise<Decision|null> {
    try {
        return await apiClient.get<Decision>(`/api/v1/decisions/${id}`)
    } catch (e) {
        return null;
    }
}

export async function getDecisionsByCaseId(id: string): Promise<Decision[]> {
    try {
        return await apiClient.get<Decision[]>(`/api/v1/decisions/case/${id}`)
    } catch (e) {
        return [];
    }
}