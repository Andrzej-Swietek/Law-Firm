package pl.swietek.law_firm.reponses

data class TopClient(
    val fullName: String,
    val trialCount: Int,
) {
}

data class TopClientResponse(
    val topClients: List<TopClient>,
)