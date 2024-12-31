import type {Trial} from "$lib/interfaces/trial.interface";

export interface RequiredDocumentForTrial {
    id: number;
    trialId: number;
    documentId: number;

    trial?: Trial;
    document?: Document;
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