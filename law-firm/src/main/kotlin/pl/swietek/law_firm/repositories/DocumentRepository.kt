package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.DocumentMapper
import pl.swietek.law_firm.mappers.DocumentTypeMapper
import pl.swietek.law_firm.models.Document
import pl.swietek.law_firm.models.DocumentType

@Repository
class DocumentRepository(private val jdbcTemplate: JdbcTemplate) {

    private val documentRowMapper = DocumentMapper()
    private val documentTypeRowMapper = DocumentTypeMapper()

    fun getDocumentsWithAllRelations(page: Int, size: Int): List<Pair<Document, DocumentType>> {
        val offset = (page - 1) * size
        val sql = """
            SELECT 
                d.id AS document_id, 
                d.file_path, 
                dt.id AS type_id, 
                dt.name AS type_name
            FROM LawFirm.document d
            JOIN LawFirm.document_types dt ON d.type_id = dt.id
            LIMIT ? OFFSET ?
        """.trimIndent()

        return jdbcTemplate.query(sql) { rs, _ ->
            val document = documentRowMapper.mapRow(rs, 1)
            val type = documentTypeRowMapper.mapRow(rs, 1)
            document to type
        }
    }

    fun getDocumentWithAllRelationsById(documentId: Long): Pair<Document, DocumentType>? {
        val sql = """
            SELECT 
                d.id AS document_id, 
                d.file_path, 
                dt.id AS type_id, 
                dt.name AS type_name
            FROM LawFirm.document d
            JOIN LawFirm.document_types dt ON d.type_id = dt.id
            WHERE d.id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql) { rs, _ ->
            val document = documentRowMapper.mapRow(rs, 1)
            val type = documentTypeRowMapper.mapRow(rs, 1)
            document to type
        }.firstOrNull()
    }

    fun saveDocument(document: Document): Document {
        val sql = "INSERT INTO LawFirm.document (id, type_id, file_path) VALUES (null, ?, ?)"
        jdbcTemplate.update(sql, document.typeId, document.filePath)
        return document
    }

    fun updateDocument(document: Document): Document {
        val sql = "UPDATE LawFirm.document SET type_id = ?, file_path = ? WHERE id = ?"
        jdbcTemplate.update(sql, document.typeId, document.filePath, document.id)
        return document
    }

    fun deleteDocument(documentId: Long): Boolean {
        val sql = "DELETE FROM LawFirm.document WHERE id = ?"
        return jdbcTemplate.update(sql, documentId) > 0
    }
}
