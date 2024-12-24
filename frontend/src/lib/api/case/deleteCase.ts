import apiClient from "$lib/services/apiClient";

export const deleteCase = async (id: number|string): Promise<{ message: string }> => {
    try {
        await apiClient.delete(`/api/v1/cases/${id}`)
        return { message: "success" }
    } catch (e) {
        return { message: "failure" }
    }
}