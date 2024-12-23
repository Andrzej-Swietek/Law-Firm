import type {Client, Lawyer} from "$lib/interfaces/person.interface";

export interface Case {
    id: number;
    name: string;
    description: string;
    responsibleLawyerId: number;
    clientId: number;

    client?: Client;
    responsibleLawyer?: Lawyer;
}