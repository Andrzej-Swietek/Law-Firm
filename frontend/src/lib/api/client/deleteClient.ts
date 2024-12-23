import apiClient from "$lib/services/apiClient";

export async function deleteClient(clientId: string|number): Promise<{ message: string }> {
    try {
        await apiClient.delete<void>(`/api/v1/appeals/${clientId}`);
        return { message: "success" }
    } catch (e) {
        return { message: "failure" }
    }
}