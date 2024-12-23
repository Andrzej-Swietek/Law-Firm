import type {Ruling} from "$lib/interfaces/ruling.interface";
import type {Trial} from "$lib/interfaces/trial.interface";

export interface Appeal {
    id: number,
    initialRulingId: number,
    finalRulingId: number,
    trialId: number,

    initialRuling?: Ruling;
    finalRuling?: Ruling;
    trial?: Trial;
}