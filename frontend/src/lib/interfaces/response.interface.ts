export interface PaginatedResponse<T> {
    data: T[];
    currentPage: number;
    size: number;
    totalCount: number;
}