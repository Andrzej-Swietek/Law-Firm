package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Case
import java.sql.ResultSet

@Service
class CaseMapper(
    private val lawyerMapper: LawyerMapper,
    private val clientMapper: ClientMapper,
) : RowMapper<Case> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Case {
        return Case(
            id = rs.getInt("case_id"),
            name = rs.getString("case_name"),
            description = rs.getString("case_description"),
            responsibleLawyerId = rs.getInt("case_responsible_lawyer_id"),
            clientId = rs.getInt("case_client_id"),
            responsibleLawyer = lawyerMapper.mapRow(rs, rowNum),
            client = clientMapper.mapRow(rs, rowNum),
            documents = emptyList()
        )
    }
}