import type {Client, Judge, Lawyer} from "$lib/interfaces/person.interface";
import type {Case} from "$lib/interfaces/case.interface";

export interface Trial {
    id: number,
    title: string,
    description: string,
    trialStatusId: number,
    clientId: number,
    lawyerId: number,
    judgeId: number,
    date: string,
    caseId: number,

    trialStatus?: TrialStatus
    client?: Client
    lawyer?: Lawyer,
    judge?: Judge
    case?: Case
}

export interface TrialStatus {
    id: number;
    name: string;
}