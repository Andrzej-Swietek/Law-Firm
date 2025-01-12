import type {Client} from "$lib/interfaces/person.interface";
import apiClient from "$lib/services/apiClient";
import type {Trial} from "$lib/interfaces/trial.interface";


export async function getTrialById(id: string|number): Promise<Trial|null> {
    try {
        return await apiClient.get<Trial>(`/api/v1/trials/${id}`)
    } catch (e) {
        return null;
    }
}

export async function getTrialByClientId(id: string|number): Promise<Trial[]> {
    try {
        return await apiClient.get<Trial[]>(`/api/v1/trials/client/${id}`)
    } catch (e) {
        return [];
    }
}

export async function getTrialByLawyerId(id: string|number): Promise<Trial[]> {
    try {
        return await apiClient.get<Trial[]>(`/api/v1/trials/lawyer/${id}`)
    } catch (e) {
        return [];
    }
}