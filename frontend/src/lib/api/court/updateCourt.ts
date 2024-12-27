import type {CourtDivision} from "$lib/interfaces/court.interface";
import type {Judge} from "$lib/interfaces/person.interface";
import apiClient from "$lib/services/apiClient";

export const updateCourt = async (id: number, payload: Partial<CourtDivision>): Promise<CourtDivision|null> => {
    try {
        const result: CourtDivision =  await apiClient.put<CourtDivision>(`/api/v1/courts/${id}`, payload);
        return result;
    } catch (e) {
        return null
    }
}