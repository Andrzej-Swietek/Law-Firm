import apiClient from "$lib/services/apiClient";
import type {Appeal} from "$lib/interfaces/appeal.interface";


type AppealDTO = Partial<Appeal>

export async function createAppeal(appeal: AppealDTO): Promise<Appeal|null> {
    try {
        const result =  await apiClient.post<Appeal>(`/api/v1/appeals`, appeal);
        return result;
    } catch (e) {
        return null
    }
}