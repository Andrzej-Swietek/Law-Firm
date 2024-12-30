import apiClient from "$lib/services/apiClient";

export async function deleteRequiredDocument(id: string|number): Promise<{ message: string }> {
    try {
        await apiClient.delete<void>(`/api/v1/documents/required-documents-for-trials/${id}`);
        return { message: "success" }
    } catch (e) {
        return { message: "failure" }
    }
}