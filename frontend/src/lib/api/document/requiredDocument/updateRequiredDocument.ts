import apiClient from "$lib/services/apiClient";
import type {RequiredDocumentForTrial} from "$lib/interfaces/document.interface";

type RequiredDocumentForTrialDTO = Partial<RequiredDocumentForTrial>

export async function updateRequiredDocumentForTrial(id: number, payload: RequiredDocumentForTrialDTO): Promise<RequiredDocumentForTrial|null> {
    try {
        const result =  await apiClient.put<RequiredDocumentForTrial>(
            `/api/v1/documents/required-documents-for-trials/${id}`,
            payload
        );
        return result;
    } catch (e) {
        return null
    }
}