import apiClient from "$lib/services/apiClient";
import type {CourtDivision} from "$lib/interfaces/court.interface";

export async function getCourtById(id: string): Promise<CourtDivision|null> {
    try {
        return await apiClient.get<CourtDivision>(`/api/v1/courts/${id}`)
    } catch (e) {
        return null;
    }
}