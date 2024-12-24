import type {Judge} from "$lib/interfaces/person.interface";
import apiClient from "$lib/services/apiClient";

export async function getJudgeById(id: string): Promise<Judge|null> {
    try {
        return await apiClient.get<Judge>(`/api/v1/judges/${id}`)
    } catch (e) {
        return null;
    }
}