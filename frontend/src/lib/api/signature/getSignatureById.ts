import apiClient from "$lib/services/apiClient";
import type {Signature} from "$lib/interfaces/signature.interface";


export async function getSignatureById(id: string): Promise<Signature|null> {
    try {
        return await apiClient.get<Signature>(`/api/v1/signatures/${id}`)
    } catch (e) {
        return null;
    }
}