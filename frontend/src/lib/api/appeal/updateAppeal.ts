import type {Appeal} from "$lib/interfaces/appeal.interface";
import apiClient from "$lib/services/apiClient";

type AppealDTO = Partial<Appeal>

export async function updateAppeal(id: number, appeal: AppealDTO): Promise<Appeal|null> {
    try {
        const result: Appeal =  await apiClient.put<Appeal>(`/api/v1/appeals/${id}`, appeal);
        return result;
    } catch (e) {
        return null
    }
}