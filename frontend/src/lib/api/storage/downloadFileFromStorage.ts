import apiClient, {BASE_URL} from "$lib/services/apiClient";

export const downloadFileFromStorage = async(storagePath: string): Promise<any> => {

    try {
        const response = await fetch(
            `${BASE_URL}/api/v1/storage/download?filePath=${encodeURIComponent(storagePath)}`,
            {
                headers: {
                    accept: 'application/octet-stream',
                }
            }
        );

        const blob = await response.blob();

        const fileURL = window.URL.createObjectURL(blob);

        const link = document.createElement('a');
        link.href = fileURL;
        link.setAttribute('download', storagePath.split('/').pop() || 'file');
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        return true;

    } catch (e) {
        return null;
    }

}