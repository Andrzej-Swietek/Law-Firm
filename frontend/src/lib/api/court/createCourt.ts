import apiClient from "$lib/services/apiClient";
import type {CourtDivision} from "$lib/interfaces/court.interface";

type CourtDTO = Partial<CourtDivision>

export async function createCourt(court: CourtDTO): Promise<CourtDivision|null> {
    try {
        const result =  await apiClient.post<CourtDivision>(`/api/v1/courts`, court);
        return result;
    } catch (e) {
        return null
    }
}