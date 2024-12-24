import apiClient from "$lib/services/apiClient";
import type {Decision} from "$lib/interfaces/decision.interface";

export async function getAllDecisions(page: number=1, limit: number=10): Promise<Decision[]> {
    try {
        return await apiClient.get<Decision[]>(`/api/v1/decisions/all?page=${page}&size=${limit}`)
    } catch (e) {
        return [];
    }
}