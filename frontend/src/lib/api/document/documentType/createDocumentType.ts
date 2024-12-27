import type {DocumentType} from "$lib/interfaces/document.interface";
import apiClient from "$lib/services/apiClient";

type DocumentTypeDTO = { name: string };
export async function createDocumentType(documentType: DocumentTypeDTO): Promise<DocumentType|null> {
    try {
        const result =  await apiClient.post<DocumentType>(`/api/v1/document-types`, documentType);
        return result;
    } catch (e) {
        return null
    }
}