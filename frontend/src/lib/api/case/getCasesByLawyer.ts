import type {Case} from "$lib/interfaces/case.interface";
import apiClient from "$lib/services/apiClient";

export async function getCasesByLawyer(lawyerId: string): Promise<Case[]> {
    try {
        return await apiClient.get<Case[]>(`/api/v1/cases/lawyer/${lawyerId}`)
    } catch (e) {
        return [];
    }
}