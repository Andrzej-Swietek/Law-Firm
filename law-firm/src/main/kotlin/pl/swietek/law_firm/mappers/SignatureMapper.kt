package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.*
import pl.swietek.law_firm.reponses.PersonResponse
import java.sql.ResultSet

@Service
class SignatureMapper(

    private val requiredDocumentMapper: RequiredDocumentForTrialMapper,
    private val documentMapper: DocumentMapper,
    private val trialMapper: TrialMapper
) : RowMapper<Signature> {

    override fun mapRow(rs: ResultSet, rowNum: Int): Signature {
        val requiredDocument = requiredDocumentMapper.mapRow(rs, 1)
        val trial = trialMapper.mapMinimalTrial(rs)
        val document = documentMapper.mapBriefDocument(rs)

        return Signature(
            id = rs.getInt("id"),
            personId = rs.getInt("person_id"),
            role = rs.getString("role"),
            requiredDocumentId = rs.getInt("required_document_id"),
            date = rs.getDate("date").toLocalDate(),
            person = getPerson(rs)?.toPerson(),
            requiredDocument = requiredDocument.copy(
                trial = trial,
                document = document,
            )
        )
    }

    private fun getPerson(rs: ResultSet): PersonResponse? {
        val role = rs.getString("role")
        return when (role) {
            "client" -> PersonResponse(
                rs.getInt("person_id"),
                rs.getString("person_first_name"),
                rs.getString("person_last_name"),
            )
            "lawyer" -> PersonResponse(
                rs.getInt("person_id"),
                rs.getString("person_first_name"),
                rs.getString("person_last_name"),
            )
            "judge" -> PersonResponse(
                rs.getInt("person_id"),
                rs.getString("person_first_name"),
                rs.getString("person_last_name"),
            )
            else -> null
        }
    }
}