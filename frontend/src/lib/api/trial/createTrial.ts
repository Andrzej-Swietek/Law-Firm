import apiClient from "$lib/services/apiClient";
import type {Trial} from "$lib/interfaces/trial.interface";

type TrialDTO = Partial<Trial>

export async function createTrial(trial: TrialDTO): Promise<Trial|null> {
    try {
        const result: Trial =  await apiClient.post<Trial>(`/api/v1/trials`, trial);
        return result;
    } catch (e) {
        return null
    }
}