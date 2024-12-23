import apiClient from "$lib/services/apiClient";
import type {Lawyer} from "$lib/interfaces/person.interface";

type LawyerDTO = Partial<Lawyer>

export async function updateLawyer(id: number, lawyer: LawyerDTO): Promise<Lawyer|null> {
    try {
        const result: Lawyer =  await apiClient.put<Lawyer>(`/api/v1/lawyers/${id}`, lawyer);
        return result;
    } catch (e) {
        return null
    }
}