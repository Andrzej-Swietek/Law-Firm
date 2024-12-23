import type {Trial} from "$lib/interfaces/trial.interface";

export interface Ruling {
    id: number,
    isFinal: boolean,
    content: string,
    trialId: number,
    finalizationDate: string;

    trial?: Trial;
}