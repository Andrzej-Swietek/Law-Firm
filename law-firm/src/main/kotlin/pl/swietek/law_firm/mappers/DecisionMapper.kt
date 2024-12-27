package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Case
import pl.swietek.law_firm.models.Decision
import java.sql.ResultSet

@Service
class DecisionMapper(private val caseMapper: CaseMapper) : RowMapper<Decision> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Decision {
        return Decision(
            id = rs.getInt("decision_id"),
            name = rs.getString("decision_name"),
            description = rs.getString("decision_description"),
            date = rs.getDate("decision_date").toLocalDate(),
            caseId = rs.getInt("decision_case_id"),

            case = caseMapper.mapBriefCase(rs, "case_")
        )
    }
}