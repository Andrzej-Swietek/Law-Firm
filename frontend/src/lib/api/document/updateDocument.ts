import apiClient from "$lib/services/apiClient";
import type {Document} from "$lib/interfaces/document.interface";

// @ts-ignore
const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

type DocumentDTO = Partial<Document & { file?: File|null }>

export async function updateDocument(id: number, document: DocumentDTO): Promise<Document|null> {
    try {
        const formData = new FormData();
        formData.append("title", document.title || "");
        formData.append("description", document.description || "");
        formData.append("typeId", String(document.typeId || 1));
        if (document.file)
            formData.append("file", document.file);

        if (formData) {
            const response = await fetch(`${BASE_URL}/api/v1/documents/${id}`, {
                method: 'PUT',
                headers: {
                    ContentType: "multipart/form-data"
                },
                body: formData
            });
            return await response.json()
        }
        return null

        // const result =  await apiClient.post<Document>(`/api/v1/documents/${id}`, document);
        // return result;
    } catch (e) {
        return null
    }
}