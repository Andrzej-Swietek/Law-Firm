import apiClient from "$lib/services/apiClient";
import type {Appeal} from "$lib/interfaces/appeal.interface";


export async function getAppealById(appealId: string): Promise<Appeal|null> {
    try {
        return await apiClient.get<Appeal>(`/api/v1/appeals/${appealId}`)
    } catch (e) {
        return null;
    }
}