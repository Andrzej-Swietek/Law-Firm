package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Document
import java.sql.ResultSet

@Service
class DocumentMapper : RowMapper<Document> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Document {
        return Document(
            id = rs.getLong("id"),
            typeId = rs.getLong("type_id"),
            filePath = rs.getString("file_path")
        )
    }
}