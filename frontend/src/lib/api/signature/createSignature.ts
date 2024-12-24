import apiClient from "$lib/services/apiClient";
import type {Signature} from "$lib/interfaces/signature.interface";


type SignatureDTO = Partial<Signature>

export async function createSignature(payload: SignatureDTO): Promise<Signature|null> {
    try {
        const result =  await apiClient.post<Signature>(`/api/v1/signatures`, payload);
        return result;
    } catch (e) {
        return null
    }
}