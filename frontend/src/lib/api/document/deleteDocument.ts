import apiClient from "$lib/services/apiClient";

export async function deleteDocument(id: string|number): Promise<{ message: string }> {
    try {
        await apiClient.delete<void>(`/api/v1/documents/${id}`);
        return { message: "success" }
    } catch (e) {
        return { message: "failure" }
    }
}