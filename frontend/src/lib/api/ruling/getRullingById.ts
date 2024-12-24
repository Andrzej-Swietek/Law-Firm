import apiClient from "$lib/services/apiClient";
import type {Ruling} from "$lib/interfaces/ruling.interface";

export async function getRulingById(id: string): Promise<Ruling|null> {
    try {
        return await apiClient.get<Ruling>(`/api/v1/rulings/${id}`)
    } catch (e) {
        return null;
    }
}