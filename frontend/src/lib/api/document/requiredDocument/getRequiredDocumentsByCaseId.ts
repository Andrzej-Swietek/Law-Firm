import apiClient from "$lib/services/apiClient";
import type {RequiredDocumentForTrial} from "$lib/interfaces/document.interface";

export const getRequiredDocumentsByCaseId = async(caseId: number|string): Promise<RequiredDocumentForTrial[]> => {
    try {
        return await apiClient.get<RequiredDocumentForTrial[]>(
            `/api/v1/documents/required-documents-for-trials/for-case/${caseId}`
        )
    } catch (e) {
        return [];
    }
}