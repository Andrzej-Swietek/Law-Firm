package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Person
import pl.swietek.law_firm.models.Signature
import java.sql.ResultSet

@Service
class SignatureMapper(
    private val clientMapper: ClientMapper,
    private val lawyerMapper: LawyerMapper,
    private val judgeMapper: JudgeMapper
) : RowMapper<Signature> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Signature {
        return Signature(
            id = rs.getInt("signature_id"),
            personId = rs.getInt("person_id"),
            role = rs.getString("role"),
            requiredDocumentId = rs.getInt("required_document_id"),
            person = getPerson(rs)
        )
    }

    private fun getPerson(rs: ResultSet): Person? {
        val role = rs.getString("role")
        return when (role) {
            "client" -> clientMapper.mapRow(rs, 0)
            "lawyer" -> lawyerMapper.mapRow(rs, 0)
            "judge" -> judgeMapper.mapRow(rs, 0)
            else -> null
        }
    }
}