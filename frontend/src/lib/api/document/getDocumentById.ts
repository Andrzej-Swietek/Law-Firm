
import apiClient from "$lib/services/apiClient";
import type { Document, DocumentType} from "$lib/interfaces/document.interface";

type DocumentResponse = {
    document : Partial<Document>,
    documentType: Partial<DocumentType>
}

export async function getDocumentById(id: string|number): Promise<DocumentResponse|null> {
    try {
        return await apiClient.get<DocumentResponse>(`/api/v1/documents/${id}`)
    } catch (e) {
        return null;
    }
}