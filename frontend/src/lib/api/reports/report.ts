import type {Lawyer} from "$lib/interfaces/person.interface";
import apiClient from "$lib/services/apiClient";

export async function getUnsignedDocuments(): Promise<any[]> {
    try {
        return await apiClient.get<any[]>(`/api/v1/reports/unsigned-documents`)
    } catch (e) {
        return [];
    }
}


export type TrialDetailsResponse = {
    trial_title: string,
    trial_date: string,
    status: string,
    required_documents_count: number
}
export async function getReportTrialDetails(): Promise<TrialDetailsResponse[]> {
    try {
        return await apiClient.get<TrialDetailsResponse[]>(`/api/v1/reports/trial-details`)
    } catch (e) {
        return [];
    }
}


export type TopClientsResponse = {
    topClients: { fullName: string, trialCount: number }[]
}
export async function getReportTopClients(): Promise<TopClientsResponse> {
    try {
        return await apiClient.get<TopClientsResponse>(`/api/v1/reports/top-clients`)
    } catch (e) {
        return { topClients: [] };
    }
}



export type ReportLawyerCasesResponse = {
    lawyers: { fullName: string, trialCount: number }[],
    count: number
}
export async function getReportLawyerCases(minCases: number = 0): Promise<ReportLawyerCasesResponse> {
    try {
        return await apiClient.get<ReportLawyerCasesResponse>(`/api/v1/reports/lawyer-cases?minCases=${minCases}`)
    } catch (e) {
        return { lawyers: [], count: 0 };
    }
}


export type ReportCaseDecision = {
    case_name: string,
    decision_name: string,
    decision_description: string,
    decision_date: string
}
export async function getReportCaseDecision(fromDate: string): Promise<ReportCaseDecision[]> {
    try {
        return await apiClient.get<ReportCaseDecision[]>(`/api/v1/reports/case-decisions?fromDate=${fromDate}`)
    } catch (e) {
        return [];
    }
}


export type ClientPayment = {
    id: number,
    firstName: string,
    lastName: string,
    email: string,
    contactDataId: number,
    payment: number
}
export const getAllClientsMonthlyPayment = async (): Promise<ClientPayment[]> => {
    try {
        return await apiClient.get(`/api/v1/reports/monthly-payments/all`)
    } catch (e) {
        return [];
    }
}

export const getClientMonthlyPayment = async (clientId: number): Promise<ClientPayment[]> => {
    try {
        return await apiClient.get(`/api/v1/reports/monthly-payments/${clientId}`)
    } catch (e) {
        return [];
    }
}