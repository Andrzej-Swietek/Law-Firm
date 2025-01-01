// /required-documents-for-trials/by-case/{id}

import apiClient from "$lib/services/apiClient";
import type {Case} from "$lib/interfaces/case.interface";

export async function getCasesForRequiredDocument(docId: number|string): Promise<Case[]> {
    try {
        return await apiClient.get<Case[]>(
            `/api/v1/documents/required-documents-for-trials/by-case/${docId}`
        )
    } catch (e) {
        return [];
    }
}