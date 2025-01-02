package pl.swietek.law_firm.repositories.queryBuilders

class SignatureQueryBuilder : QueryBuilder() {

    private val fields = mutableListOf<String>()
    private val joins = mutableListOf<String>()

    fun selectBasic(): SignatureQueryBuilder {
        fields.addAll(
            listOf(
                "s.id AS id",
                "s.person_id AS person_id",
                "s.role AS role",
                "s.required_document_id AS required_document_id",
                "s.date AS date"
            )
        )
        return this
    }

    fun withPersonData(): SignatureQueryBuilder {
        fields.addAll(
            listOf(
                "pd.first_name AS person_first_name",
                "pd.last_name AS person_last_name"
            )
        )
        joins.add(
            """
            LEFT JOIN LawFirm.get_signature_person_data(s.id) pd ON TRUE
            """.trimIndent()
        )
        return this
    }

    fun withRequiredDocument(): SignatureQueryBuilder {
        val reqDocQueryBuilder = RequiredDocumentQueryBuilder()
            .selectBasic()
            .withDocument()

        joins.add(
            """
            LEFT JOIN LawFirm.required_documents_for_trial rdft ON s.required_document_id = rdft.id
            """.trimIndent()
        )
        fields.addAll(reqDocQueryBuilder.getFields())
        joins.addAll(reqDocQueryBuilder.getJoins())
        return this
    }

    fun withTrial(): SignatureQueryBuilder {
        val trialQueryBuilder = TrialQueryBuilder()
            .selectBasic()
            .withCase()

        fields.addAll(
            trialQueryBuilder.getFields()
        )
        joins.add(
            """
                LEFT JOIN LawFirm.trial t ON rdft.trial_id = t.id
            """.trimIndent()
        )
        joins.addAll(
            trialQueryBuilder.getJoins()
        )
        return this
    }

    override fun build(): String {
//        val sql = """
//            SELECT ${this.getFields().joinToString(", ")}
//            FROM LawFirm.signature s
//            ${this.getJoins().joinToString(" ")}
//        """.trimIndent()
        val selectClause = if (fields.isNotEmpty()) "SELECT ${fields.joinToString(", ")}" else "SELECT *"
        val fromClause = "FROM LawFirm.signature s"
        val joinClause = if (joins.isNotEmpty()) joins.joinToString(" ") else ""
        val whereClause = if (getConditions().isNotEmpty()) "WHERE ${getConditions().joinToString(" AND ")}" else ""
        val groupByClause = if (getGroupBys().isNotEmpty()) "GROUP BY ${getGroupBys().joinToString(", ")}" else ""
        val orderByClause = if (getOrderBys().isNotEmpty()) "ORDER BY ${getOrderBys().joinToString(", ")}" else ""

        return listOf(selectClause, fromClause, joinClause, whereClause, groupByClause, orderByClause)
            .filter { it.isNotBlank() }
            .joinToString(" ")
    }


    override fun getFields(): List<String> = fields
    override fun getJoins(): List<String> = joins

}