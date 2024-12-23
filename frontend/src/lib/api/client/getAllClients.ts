import apiClient from "$lib/services/apiClient";
import type {Client} from "$lib/interfaces/person.interface";

export async function getAllClients(page: number=1, limit: number=10): Promise<Client[]> {
    try {
        return await apiClient.get<Client[]>(`/api/v1/client/all?page=${page}&size=${limit}`)
    } catch (e) {
        return [];
    }
}