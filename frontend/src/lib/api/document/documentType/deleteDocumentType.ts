import apiClient from "$lib/services/apiClient";

export async function deleteDocumentType(id: string|number): Promise<{ message: string }> {
    try {
        await apiClient.delete<void>(`/api/v1/document-types/${id}`);
        return { message: "success" }
    } catch (e) {
        return { message: "failure" }
    }
}