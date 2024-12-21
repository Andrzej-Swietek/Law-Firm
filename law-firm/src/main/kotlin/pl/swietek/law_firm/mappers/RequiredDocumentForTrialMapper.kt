package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.RequiredDocumentForTrial
import java.sql.ResultSet

@Service
class RequiredDocumentForTrialMapper : RowMapper<RequiredDocumentForTrial> {
    override fun mapRow(rs: ResultSet, rowNum: Int): RequiredDocumentForTrial {
        return RequiredDocumentForTrial(
            id = rs.getInt("required_document_id"),
            trialId = rs.getInt("trial_id"),
            documentId = rs.getInt("document_id")
        )
    }
}