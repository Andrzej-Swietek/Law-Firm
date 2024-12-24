import apiClient from "$lib/services/apiClient";
import type {Signature} from "$lib/interfaces/signature.interface";


export async function getSignatureByCase(id: string): Promise<Signature[]> {
    try {
        return await apiClient.get<Signature[]>(`/api/v1/signatures/case/${id}`)
    } catch (e) {
        return [];
    }
}