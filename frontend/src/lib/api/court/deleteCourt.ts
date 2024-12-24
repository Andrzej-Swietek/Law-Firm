import apiClient from "$lib/services/apiClient";

export const deleteCourt = async (id: number|string): Promise<{ message: string }>=> {
    try {
        await apiClient.delete(`/api/v1/courts/${id}`)
        return { message: "success" }
    } catch (e) {
        return { message: "failure" }
    }
}