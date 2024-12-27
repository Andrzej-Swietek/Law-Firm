import apiClient from "$lib/services/apiClient";
import type {Decision} from "$lib/interfaces/decision.interface";

type DecisionDTO = Partial<Decision>

export async function updateDecision(id: number, decision: DecisionDTO): Promise<Decision|null> {
    try {
        const result: Decision =  await apiClient.put<Decision>(`/api/v1/decisions/${id}`, {decision, caseId: decision.caseID});
        return result;
    } catch (e) {
        return null
    }
}