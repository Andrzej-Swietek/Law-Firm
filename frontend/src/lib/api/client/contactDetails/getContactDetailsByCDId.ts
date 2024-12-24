import apiClient from "$lib/services/apiClient";
import type {ContactDetails} from "$lib/interfaces/person.interface";

export const getContactDetailsByCDId = async (contactDetailsID: number|string): Promise<ContactDetails|null> => {
    try {
        return await apiClient.get<ContactDetails>(`/api/v1/contact-details/${contactDetailsID}`)
    } catch (e) {
        return null;
    }
}