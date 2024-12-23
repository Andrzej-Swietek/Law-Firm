import apiClient from "$lib/services/apiClient";
import type {Lawyer} from "$lib/interfaces/person.interface";


type LawyerDTO = Partial<Lawyer>

export async function createLawyer(lawyer: LawyerDTO): Promise<Lawyer|null> {
    try {
        const result =  await apiClient.post<Lawyer>(`/api/v1/lawyers`, lawyer);
        return result;
    } catch (e) {
        return null
    }
}