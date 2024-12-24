import apiClient from "$lib/services/apiClient";
import type {Signature} from "$lib/interfaces/signature.interface";


type SignatureDTO = Partial<Signature>

export async function updateSignature(id: number, decision: SignatureDTO): Promise<Signature|null> {
    try {
        const result: Signature =  await apiClient.put<Signature>(`/api/v1/signatures/${id}`, decision);
        return result;
    } catch (e) {
        return null
    }
}