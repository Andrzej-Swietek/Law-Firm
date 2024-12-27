import apiClient from "$lib/services/apiClient";
import type {TrialStatus} from "$lib/interfaces/trial.interface";

export const getAllTrialStatuses = async() => {
    try {
        return await apiClient.get<TrialStatus[]>(`/api/v1/trial-statuses/all`)
    } catch (e) {
        return [];
    }
}