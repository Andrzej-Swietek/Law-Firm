import apiClient from "$lib/services/apiClient";

export async function deleteLawyer(id: string|number): Promise<{ message: string }> {
    try {
        await apiClient.delete<void>(`/api/v1/lawyers/${id}`);
        return { message: "success" }
    } catch (e) {
        return { message: "failure" }
    }
}