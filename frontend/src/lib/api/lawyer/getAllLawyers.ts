import type {Appeal} from "$lib/interfaces/appeal.interface";
import apiClient from "$lib/services/apiClient";
import type {Lawyer} from "$lib/interfaces/person.interface";

export async function getAllLawyers(page: number=1, limit: number=10): Promise<Lawyer[]> {
    try {
        return await apiClient.get<Lawyer[]>(`/api/v1/lawyers/all?page=${page}&size=${limit}`)
    } catch (e) {
        return [];
    }
}