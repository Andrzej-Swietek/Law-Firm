import apiClient from "$lib/services/apiClient";
import type {CourtDivision} from "$lib/interfaces/court.interface";

export async function getAllCourts(): Promise<CourtDivision[]> {
    try {
        return await apiClient.get<CourtDivision[]>(`/api/v1/courts/all`)
    } catch (e) {
        return [];
    }
}