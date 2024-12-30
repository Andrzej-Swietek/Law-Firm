import apiClient from "$lib/services/apiClient";
import type {Signature} from "$lib/interfaces/signature.interface";

export const getAllSignatures = async(page: number, size: number): Promise<Signature[]> => {
    return await apiClient.get<Signature[]>(`/api/v1/signatures/all?page=${page}&size=${size}`);
}