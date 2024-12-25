import apiClient from "$lib/services/apiClient";
import type {Ruling} from "$lib/interfaces/ruling.interface";

export async function getAllRulings(page: number=1, limit: number=10): Promise<Ruling[]> {
    try {
        return await apiClient.get<Ruling[]>(`/api/v1/rulings/all?page=${page}&size=${limit}`)
    } catch (e) {
        return [];
    }
}

export async function getAllRulingsAllPages(): Promise<Ruling[]> {
    let page = 1;
    const limit = 10000;
    let allRulings: Ruling[] = [];
    let fetchedRulings: Ruling[];

    do {
        fetchedRulings = await getAllRulings(page, limit);
        allRulings = allRulings.concat(fetchedRulings);
        page++;
    } while (fetchedRulings.length === limit);

    return allRulings;
}