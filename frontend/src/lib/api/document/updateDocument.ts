import apiClient from "$lib/services/apiClient";
import type {Document} from "$lib/interfaces/document.interface";

type DocumentDTO = Partial<Document & { file?: File }>

export async function updateDocument(id: number, document: DocumentDTO): Promise<Document|null> {
    try {
        const result =  await apiClient.post<Document>(`/api/v1/documents/${id}`, document);
        return result;
    } catch (e) {
        return null
    }
}