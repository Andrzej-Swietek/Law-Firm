package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.DocumentMapper
import pl.swietek.law_firm.mappers.DocumentTypeMapper
import pl.swietek.law_firm.models.Document
import pl.swietek.law_firm.models.DocumentType
import pl.swietek.law_firm.reponses.DocumentResponse
import pl.swietek.law_firm.requests.DocumentRequest

@Repository
class DocumentRepository(private val jdbcTemplate: JdbcTemplate) {

    private val documentRowMapper = DocumentMapper()
    private val documentTypeRowMapper = DocumentTypeMapper()

    fun getDocumentsWithAllRelations(page: Int, size: Int): List<DocumentResponse> {
        val offset = (page - 1) * size
        val sql = """
            SELECT 
                d.id AS document_id, 
                d.file_path, 
                d.title, 
                d.description,
                d.type_id as document_type_id,
                dt.id AS type_id, 
                dt.name AS document_type_name
            FROM LawFirm.document d
            JOIN LawFirm.document_types dt ON d.type_id = dt.id
            LIMIT ? OFFSET ?
        """.trimIndent()


        return jdbcTemplate.query(sql, { rs, _ ->
            val document = documentRowMapper.mapRow(rs, 1)
            val type = documentTypeRowMapper.mapRowWithPrefix(rs, "document_type_")
            DocumentResponse(document, type)
        }, size, offset)
    }

    fun getDocumentWithAllRelationsById(documentId: Long): Pair<Document, DocumentType>? {
        val sql = """
            SELECT 
                d.id AS document_id, 
                d.file_path, 
                d.title, 
                d.description,
                d.type_id as document_type_id,
                dt.id AS type_id, 
                dt.name AS document_type_name
            FROM LawFirm.document d
            JOIN LawFirm.document_types dt ON d.type_id = dt.id
            WHERE d.id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql,{ rs, _ ->
            val document = documentRowMapper.mapRow(rs, 1)
            val type = documentTypeRowMapper.mapRowWithPrefix(rs, "document_type_")
            document to type
        }, documentId).firstOrNull()
    }

    fun saveDocument(document: Document): Document {
        val sql = "INSERT INTO LawFirm.document (type_id, title, description, file_path) VALUES (?, ?, ?, ?)"
        jdbcTemplate.update(sql, document.typeId, document.title, document.description, document.filePath)
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

    fun countDocuments(): Long {
        val sql = "SELECT COUNT(*) FROM LawFirm.document"
        return jdbcTemplate.queryForObject(sql, Long::class.java) ?: 0
    }
}
