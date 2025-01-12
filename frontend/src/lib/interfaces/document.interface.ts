import type {Trial} from "$lib/interfaces/trial.interface";
import type {Signature} from "$lib/interfaces/signature.interface";

export interface RequiredDocumentForTrial {
    id: number;
    trialId: number;
    documentId: number;

    trial?: Trial;
    document?: Document;
    signatures?: Signature[]
}

export interface DocumentType {
    id: number;
    name: string;
}

export interface Document {
    id: number;
    typeId: number;
    filePath?: string;
    description?: string;
    title: string;

    documentType?: DocumentType;
}