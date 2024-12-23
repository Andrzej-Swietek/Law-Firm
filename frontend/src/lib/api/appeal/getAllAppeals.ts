import type {Appeal} from "$lib/interfaces/appeal.interface";
import apiClient from "$lib/services/apiClient";


export async function getAllAppeals(page: number=1, limit: number=10): Promise<Appeal[]> {
    try {
        return await apiClient.get<Appeal[]>(`/api/v1/appeals?page=${page}&size=${limit}`)
    } catch (e) {
        return [];
    }
}