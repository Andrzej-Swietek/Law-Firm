import apiClient from "$lib/services/apiClient";
import type {Client} from "$lib/interfaces/person.interface";

export async function getClientById(clientId: string|number): Promise<Client|null> {
    try {
        return await apiClient.get<Client>(`/api/v1/client/${clientId}`)
    } catch (e) {
        return null;
    }
}

export async function getClientByLawyerId(lawyerId: string|number): Promise<Client[]> {
    try {
        return await apiClient.get<Client[]>(`/api/v1/client/by-lawyer/${lawyerId}`)
    } catch (e) {
        return [];
    }
}