package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.DocumentTypeMapper
import pl.swietek.law_firm.models.DocumentType

@Repository
class DocumentTypesRepository(private val jdbcTemplate: JdbcTemplate) {

    private val documentTypesRowMapper = DocumentTypeMapper()

    fun getAllDocumentTypes(): List<DocumentType> {
        val sql = """
            SELECT * 
            FROM LawFirm.document_types
        """.trimIndent()

        return jdbcTemplate.query(sql, documentTypesRowMapper)
    }

    fun getDocumentTypeById(typeId: Long): DocumentType? {
        val sql = """
            SELECT * 
            FROM LawFirm.document_types 
            WHERE id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, documentTypesRowMapper, typeId).firstOrNull()
    }

    fun saveDocumentType(documentType: DocumentType): DocumentType {
        val sql = """
            INSERT INTO LawFirm.document_types (id, name) 
            VALUES (null, ?)
        """.trimIndent()

        jdbcTemplate.update(sql, documentType.name)
        return documentType
    }

    fun updateDocumentType(documentType: DocumentType): DocumentType {
        val sql = """
            UPDATE LawFirm.document_types 
            SET name = ? 
            WHERE id = ?
        """.trimIndent()

        jdbcTemplate.update(sql, documentType.name, documentType.id)
        return documentType
    }

    fun deleteDocumentType(typeId: Int): Boolean {
        val sql = "DELETE FROM LawFirm.document_types WHERE id = ?"
        val rowsAffected = jdbcTemplate.update(sql, typeId)
        return rowsAffected > 0
    }
}
