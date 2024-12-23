import apiClient from "$lib/services/apiClient";


export async function deleteAppeal(appealId: string|number): Promise<{ message: string }> {
    try {
        await apiClient.delete<void>(`/api/v1/appeals/${appealId}`);
        return { message: "success" }
    } catch (e) {
        return { message: "failure" }
    }
}