import type {Client} from "$lib/interfaces/person.interface";
import apiClient from "$lib/services/apiClient";

type ClientDTO = Partial<Client>

export async function createClient(client: ClientDTO): Promise<Client|null> {
    try {
        const result: Client =  await apiClient.post<Client>(`/api/v1/client`, client);
        return result;
    } catch (e) {
        return null
    }
}