import apiClient from "$lib/services/apiClient";
import type {Case} from "$lib/interfaces/case.interface";

export async function getAllCases(page: number=1, limit: number=10): Promise<Case[]> {
    try {
        return await apiClient.get<Case[]>(`/api/v1/cases/all?page=${page}&size=${limit}`)
    } catch (e) {
        return [];
    }
}