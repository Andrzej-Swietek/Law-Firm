import apiClient from "$lib/services/apiClient";
import type { DocumentType } from "$lib/interfaces/document.interface";

export async function getAllDocumentTypesById(id: number|string): Promise<DocumentType|null> {
    try {
        return await apiClient.get<DocumentType>(`/api/v1/document-types/${id}`)
    } catch (e) {
        return null
    }
}