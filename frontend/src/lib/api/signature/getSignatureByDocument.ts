import apiClient from "$lib/services/apiClient";
import type {Signature} from "$lib/interfaces/signature.interface";


export async function getSignatureByDocument(id: string|number): Promise<Signature[]> {
    try {
        return await apiClient.get<Signature[]>(`/api/v1/signatures/document/${id}`)
    } catch (e) {
        return [];
    }
}