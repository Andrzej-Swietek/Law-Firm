package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.DocumentType
import java.sql.ResultSet

@Service
class DocumentTypeMapper : RowMapper<DocumentType> {
    override fun mapRow(rs: ResultSet, rowNum: Int): DocumentType {
        return DocumentType(
            id = rs.getLong("id"),
            name = rs.getString("name")
        )
    }

    fun mapRowWithPrefix(rs: ResultSet, prefix: String=""): DocumentType {
        return DocumentType(
            id = rs.getLong("${prefix}id"),
            name = rs.getString("${prefix}name")
        )
    }


}
