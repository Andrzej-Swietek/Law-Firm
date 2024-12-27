package pl.swietek.law_firm.reponses

data class PaginatedResponse<T>(
    val data: List<T> = emptyList(),
    val currentPage: Int = 0,
    val size: Int = 0,
    val totalCount: Long = 0L
) {
    companion object {
        fun <T> builder(): Builder<T> {
            return Builder()
        }
    }

    class Builder<T> {
        private var data: List<T> = emptyList()
        private var currentPage: Int = 0
        private var size: Int = 0
        private var totalCount: Long = 0L

        fun data(data: List<T>) = apply { this.data = data }
        fun currentPage(currentPage: Int) = apply { this.currentPage = currentPage }
        fun size(size: Int) = apply { this.size = size }
        fun totalCount(totalCount: Long) = apply { this.totalCount = totalCount }

        fun build(): PaginatedResponse<T> {
            return PaginatedResponse(
                data = data,
                currentPage = currentPage,
                size = size,
                totalCount = totalCount
            )
        }
    }
}

