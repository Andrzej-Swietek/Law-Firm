import apiClient from "$lib/services/apiClient";
import type {Ruling} from "$lib/interfaces/ruling.interface";

export async function getRulingById(id: string): Promise<Ruling|null> {
    try {
        return await apiClient.get<Ruling>(`/api/v1/rulings/${id}`)
    } catch (e) {
        return null;
    }
}

export async function getRulingsByCaseId(id: string): Promise<Ruling[]> {
    try {
        return await apiClient.get<Ruling[]>(`/api/v1/rulings/case/${id}`)
    } catch (e) {
        return [];
    }
}