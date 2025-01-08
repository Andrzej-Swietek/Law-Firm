//

import apiClient from "$lib/services/apiClient";
import type {RequiredDocumentForTrial} from "$lib/interfaces/document.interface";

export async function getRequiredDocumentsByTrialId(trialId: number|string): Promise<RequiredDocumentForTrial[]> {
    try {
        return await apiClient.get<RequiredDocumentForTrial[]>(
            `/api/v1/documents/required-documents-for-trials/by-trial/${trialId}`
        )
    } catch (e) {
        return [];
    }
}