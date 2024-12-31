package pl.swietek.law_firm.repositories.queryBuilders

class DocumentQueryBuilder {
    private val fields = mutableListOf<String>()
    private val joins = mutableListOf<String>()

    fun selectBasic(): DocumentQueryBuilder {
        fields.addAll(
            listOf(
                "d.id AS document_id",
                "d.type_id AS type_id",
                "d.file_path AS file_path",
                "d.title AS title",
                "d.description AS description"
            )
        )
        return this
    }

    fun withDocumentType(): DocumentQueryBuilder {
        fields.add("dt.name AS document_type_name")
        joins.add("LEFT JOIN LawFirm.document_types dt ON d.type_id = dt.id")
        return this
    }

    fun getFields(): List<String> = fields
    fun getJoins(): List<String> = joins
}
