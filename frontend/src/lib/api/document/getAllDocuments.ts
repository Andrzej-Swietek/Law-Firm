import apiClient from "$lib/services/apiClient";
import type {PaginatedResponse} from "$lib/interfaces/response.interface";
import type { Document } from "$lib/interfaces/document.interface";

type DocumentResponse = {
    document : Partial<Document>,
    documentType: Partial<DocumentType>
}

export async function getAllDocument(page: number=1, limit: number=10): Promise<PaginatedResponse<DocumentResponse>> {
    try {
        return await apiClient.get<PaginatedResponse<DocumentResponse>>(`/api/v1/documents/all?page=${page}&size=${limit}`)
    } catch (e) {
        return {
            data: [],
            currentPage: 0,
            size: 0,
            totalCount: 0
        };
    }
}