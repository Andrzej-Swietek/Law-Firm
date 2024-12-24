import type {Judge} from "$lib/interfaces/person.interface";
import apiClient from "$lib/services/apiClient";

type JudgeDTO = Partial<Judge>

export async function createJudge(judge: JudgeDTO): Promise<Judge|null> {
    try {
        const result =  await apiClient.post<Judge>(`/api/v1/judges`, judge);
        return result;
    } catch (e) {
        return null
    }
}