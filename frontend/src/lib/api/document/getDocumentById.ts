
import apiClient from "$lib/services/apiClient";

export async function getDocumentById(id: string): Promise<Document|null> {
    try {
        return await apiClient.get<Document>(`/api/v1/documents/${id}`)
    } catch (e) {
        return null;
    }
}