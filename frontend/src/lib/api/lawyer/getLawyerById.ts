import apiClient from "$lib/services/apiClient";
import type {Lawyer} from "$lib/interfaces/person.interface";

export async function getLawyerById(id: string|number): Promise<Lawyer|null> {
    try {
        return await apiClient.get<Lawyer>(`/api/v1/lawyers/${id}`)
    } catch (e) {
        return null;
    }
}