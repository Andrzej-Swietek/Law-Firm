import type {CourtDivision} from "$lib/interfaces/court.interface";

export interface Person {
    id: number;
    firstname: string;
    lastname: string;
}

export interface Client extends Person {
    email: string;
    contactDetailsId: number;

    contactDetails?: ContactDetails;
}

export interface ContactDetails {
    id?: number,
    phoneNumber: string,
    email: string,
    street: string,
    city: string,
    state: string,
    zipCode: string,
    country: string
}

export interface Lawyer extends Person {
    specialization: string;
}

export interface Judge extends Person {
    courtDivisionId: number;

    courtDivision?: CourtDivision;
}


