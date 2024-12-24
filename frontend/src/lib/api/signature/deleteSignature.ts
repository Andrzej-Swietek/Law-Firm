import apiClient from "$lib/services/apiClient";

export const deleteSignature = async (id: number|string): Promise<{ message: string }> => {
    try {
        await apiClient.delete(`/api/v1/signatures/${id}`)
        return { message: "success" }
    } catch (e) {
        return { message: "failure" }
    }
}