import apiClient from "$lib/services/apiClient";
import type {RequiredDocumentForTrial} from "$lib/interfaces/document.interface";

export async function getRequiredDocumentById(id: number|string): Promise<RequiredDocumentForTrial|null> {
    try {
        return await apiClient.get<RequiredDocumentForTrial>(
            `/api/v1/documents/required-documents-for-trials/${id}`
        )
    } catch (e) {
        return null;
    }
}