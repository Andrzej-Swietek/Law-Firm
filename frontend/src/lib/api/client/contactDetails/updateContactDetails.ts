import type { ContactDetails} from "$lib/interfaces/person.interface";
import apiClient from "$lib/services/apiClient";

type ContactDetailsDTO = Partial<ContactDetails>

export async function updateContactDetails(id: number, contactDetails: ContactDetailsDTO): Promise<ContactDetails|null> {
    try {
        const result: ContactDetails =  await apiClient.put<ContactDetails>(`/api/v1/contact-details/${id}`, contactDetails);
        return result;
    } catch (e) {
        return null
    }
}