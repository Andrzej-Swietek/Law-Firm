package pl.swietek.law_firm.reponses

data class LawyerTrialsCount(
    val fullName: String,
    val trialCount: Int,
) {
}

data class LawyerTrialsCountResponse(
    val lawyers: List<LawyerTrialsCount>,
    val count: Int,
)