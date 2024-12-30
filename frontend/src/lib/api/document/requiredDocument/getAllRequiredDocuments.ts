import apiClient from "$lib/services/apiClient";
import type {RequiredDocumentForTrial} from "$lib/interfaces/document.interface";

export async function getAllRequiredDocuments(): Promise<RequiredDocumentForTrial[]> {
    try {
        return await apiClient.get<RequiredDocumentForTrial[]>(`/api/v1/documents/required-documents-for-trials/all`)
    } catch (e) {
        return [];
    }
}