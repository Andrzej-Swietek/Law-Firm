package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Document
import pl.swietek.law_firm.models.DocumentType
import java.sql.ResultSet

@Service
class DocumentMapper : RowMapper<Document> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Document {
        return Document(
            id = rs.getLong("document_id"),
            typeId = rs.getLong("type_id"),
            filePath = rs.getString("file_path"),
            title = rs.getString("title"),
            description = rs.getString("description"),
            documentType = if (rs.getObject("type_id") != null) {
                DocumentType(
                    id = rs.getLong("type_id"),
                    name = rs.getString("document_type_name")
                )
            } else null
        )
    }

    fun mapBriefDocument(rs: ResultSet): Document {
        return Document(
            id = rs.getLong("id"),
            typeId = rs.getLong("type_id"),
            filePath = rs.getString("file_path"),
            title = rs.getString("title"),
            description = rs.getString("description"),
            documentType = null
        )
    }
}