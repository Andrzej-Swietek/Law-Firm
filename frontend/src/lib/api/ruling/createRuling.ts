import apiClient from "$lib/services/apiClient";
import type {Ruling} from "$lib/interfaces/ruling.interface";

type RulingDTO = Partial<Ruling>

export async function createRuling(payload: RulingDTO): Promise<Ruling|null> {
    try {
        const result =  await apiClient.post<Ruling>(`/api/v1/rulings`, payload);
        return result;
    } catch (e) {
        return null
    }
}