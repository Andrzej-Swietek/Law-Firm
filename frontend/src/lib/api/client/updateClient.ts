import type {Appeal} from "$lib/interfaces/appeal.interface";
import apiClient from "$lib/services/apiClient";
import type {Client} from "$lib/interfaces/person.interface";

type ClientDTO = Partial<Client>

export async function updateClient(id: number, client: ClientDTO): Promise<Client|null> {
    try {
        const result: Client =  await apiClient.put<Client>(`/api/v1/client/${id}`, client);
        return result;
    } catch (e) {
        return null
    }
}