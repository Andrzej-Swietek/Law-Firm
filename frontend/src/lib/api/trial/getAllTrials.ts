import apiClient from "$lib/services/apiClient";
import type {Trial} from "$lib/interfaces/trial.interface";

export async function getAllTrials(page: number=1, limit: number=10): Promise<Trial[]> {
    try {
        return await apiClient.get<Trial[]>(`/api/v1/trials/all?page=${page}&size=${limit}`)
    } catch (e) {
        return [];
    }
}

export async function getAllTrialsUnpaginated(): Promise<Trial[]> {
    try {
        return await apiClient.get<Trial[]>(`/api/v1/trials/all-unpaginated`)
    } catch (e) {
        return [];
    }
}