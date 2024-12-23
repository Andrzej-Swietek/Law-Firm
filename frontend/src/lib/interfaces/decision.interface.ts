import type {Case} from "$lib/interfaces/case.interface";
import type {Trial} from "$lib/interfaces/trial.interface";

export interface Decision {
    id: number;
    name: string;
    description: string;
    date: string;
    caseID: number;

    case?: Case;
}

export interface Ruling {
    id: number;
    isFinal: boolean;
    trialId: number;
    finalizationDate?: string;

    trial?: Trial;
}