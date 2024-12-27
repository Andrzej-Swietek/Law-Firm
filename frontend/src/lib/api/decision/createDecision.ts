import apiClient from "$lib/services/apiClient";
import type {Decision} from "$lib/interfaces/decision.interface";

type DecisionDTO = Partial<Decision>

export async function createDecision(decision: DecisionDTO): Promise<Decision|null> {
    try {
        const result =  await apiClient.post<Decision>(`/api/v1/decisions`, {...decision, caseId: decision.caseID});
        return result;
    } catch (e) {
        return null
    }
}