import apiClient from "$lib/services/apiClient";
import type {Trial} from "$lib/interfaces/trial.interface";

type TrialDTO = Partial<Trial>

export async function updateTrial(id: number, trial: TrialDTO): Promise<Trial|null> {
    try {
        const result: Trial =  await apiClient.put<Trial>(`/api/v1/trials/${id}`, trial);
        return result;
    } catch (e) {
        return null
    }
}