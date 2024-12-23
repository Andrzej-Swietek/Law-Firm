import apiClient from "$lib/services/apiClient";

export async function deleteTrial(id: string|number): Promise<{ message: string }> {
    try {
        await apiClient.delete<void>(`/api/v1/trials/${id}`);
        return { message: "success" }
    } catch (e) {
        return { message: "failure" }
    }
}