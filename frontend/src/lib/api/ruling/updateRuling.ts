import apiClient from "$lib/services/apiClient";
import type {Ruling} from "$lib/interfaces/ruling.interface";

type RulingDTO = Partial<Ruling>

export async function updateRuling(id: number, payload: RulingDTO): Promise<Ruling|null> {
    try {
        const result: Ruling =  await apiClient.put<Ruling>(`/api/v1/rulings/${id}`, payload);
        return result;
    } catch (e) {
        return null
    }
}