package pl.swietek.law_firm.repositories.queryBuilders

class RequiredDocumentQueryBuilder : QueryBuilder() {
    private val fields = mutableListOf<String>()
    private val joins = mutableListOf<String>()

    fun selectBasic(): RequiredDocumentQueryBuilder {
        fields.addAll(
            listOf(
                "rdft.id AS required_document_id",
                "rdft.trial_id AS rdft_trial_id",
                "rdft.document_id AS rdft_document_id"
            )
        )
        return this
    }

    fun withTrial(): RequiredDocumentQueryBuilder {

        val trialQueryBuilder = TrialQueryBuilder()
            .selectBasic()
            .withTrialStatus()
            .withClient()
            .withJudge()
            .withLawyer()
            .withCase()


        fields.addAll(trialQueryBuilder.getFields())
        joins.add("LEFT JOIN LawFirm.trial t ON rdft.trial_id = t.id")
        joins.addAll(trialQueryBuilder.getJoins())
        return this
    }

    fun withDocument(): RequiredDocumentQueryBuilder {
        val documentQueryBuilder = DocumentQueryBuilder()
            .selectBasic()
            .withDocumentType()

        fields.addAll(documentQueryBuilder.getFields())

        joins.add("LEFT JOIN LawFirm.document d ON rdft.document_id = d.id")
        joins.addAll(documentQueryBuilder.getJoins())

        return this
    }

    override fun build(): String {
        return """
            SELECT ${fields.joinToString(", ")}
            FROM LawFirm.required_documents_for_trial rdft
            ${joins.joinToString(" ")}
        """.trimIndent()
    }

    override fun getFields() = fields
    override fun getJoins() = joins
}
