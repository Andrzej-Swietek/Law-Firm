import apiClient from "$lib/services/apiClient";
import type { DocumentType } from "$lib/interfaces/document.interface";

export async function getAllDocumentTypes(): Promise<DocumentType[]> {
    try {
        return await apiClient.get<DocumentType[]>(`/api/v1/document-types/all`)
    } catch (e) {
        return []
    }
}