import FetchApiClient from '$lib/api/ApiClient';


// @ts-ignore
const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

const apiClient = new FetchApiClient(BASE_URL);

export default apiClient;