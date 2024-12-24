import type {Client, ContactDetails} from "$lib/interfaces/person.interface";
import apiClient from "$lib/services/apiClient";


export async function getContactDetailsByClient(clientId: string): Promise<ContactDetails|null> {
    try {
        return await apiClient.get<ContactDetails>(`/api/v1/contact-details/client/${clientId}`)
    } catch (e) {
        return null;
    }
}