import apiClient from "$lib/services/apiClient";

export const deleteDecision = async (id: number|string): Promise<{ message: string }> => {
    try {
        await apiClient.delete(`/api/v1/decisions/${id}`)
        return { message: "success" }
    } catch (e) {
        return { message: "failure" }
    }
}