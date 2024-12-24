import type {Judge} from "$lib/interfaces/person.interface";
import apiClient from "$lib/services/apiClient";

export async function getAllJudges(page: number=1, limit: number=10): Promise<Judge[]> {
    try {
        return await apiClient.get<Judge[]>(`/api/v1/judges/all?page=${page}&size=${limit}`)
    } catch (e) {
        return [];
    }
}