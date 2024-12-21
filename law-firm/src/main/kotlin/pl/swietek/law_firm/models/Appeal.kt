package pl.swietek.law_firm.models

data class Appeal(
    var id: Int,
    val initialRulingId: Int,
    val finalRulingId: Int,
    val trialId: Int,

    val initialRuling: Ruling? = null,
    val finalRuling: Ruling? = null,
    val trial: Trial? = null
)
