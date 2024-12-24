import apiClient from "$lib/services/apiClient";


export const deleteRuling = async (id: number|string): Promise<{ message: string }> => {
    try {
        await apiClient.delete(`/api/v1/rulings/${id}`)
        return { message: "success" }
    } catch (e) {
        return { message: "failure" }
    }
}