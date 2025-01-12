import type {Person} from "$lib/interfaces/person.interface";
import type {RequiredDocumentForTrial} from "$lib/interfaces/document.interface";

export interface Signature {
    id: number,
    personId: number,
    role: string,
    requiredDocumentId: number,
    date?: string,

    requiredDocument?: RequiredDocumentForTrial;
    person?: Person
}