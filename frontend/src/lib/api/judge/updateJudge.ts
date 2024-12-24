import type {Judge} from "$lib/interfaces/person.interface";
import apiClient from "$lib/services/apiClient";


type JudgeDTO = Partial<Judge>

export async function updateJudge(id: number, judge: JudgeDTO): Promise<Judge|null> {
    try {
        const result: Judge =  await apiClient.put<Judge>(`/api/v1/lawyers/${id}`, judge);
        return result;
    } catch (e) {
        return null
    }
}