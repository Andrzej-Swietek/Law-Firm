import type {Case} from "$lib/interfaces/case.interface";
import apiClient from "$lib/services/apiClient";

export async function getCasesByLawyer(lawyerId: string, page: number = 1, size:number = 10): Promise<Case[]> {
    try {
        return await apiClient.get<Case[]>(`/api/v1/cases/lawyer/${lawyerId}?page=${page}&size=${size}`)
    } catch (e) {
        return [];
    }
}