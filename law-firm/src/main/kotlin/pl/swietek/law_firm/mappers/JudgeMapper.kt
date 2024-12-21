package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.CourtDivision
import pl.swietek.law_firm.models.Judge
import java.sql.ResultSet

@Service
class JudgeMapper : RowMapper<Judge> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Judge {
        val courtDivision = if (rs.getObject("court_division_id") != null) {
            CourtDivision(
                id = rs.getInt("court_division_id"),
                name = rs.getString("court_division_name"),
                city = rs.getString("court_division_city")
            )
        } else {
            null
        }

        return Judge(
            id = rs.getInt("id"),
            firstName = rs.getString("first_name"),
            lastName = rs.getString("last_name"),
            courtDivisionId = rs.getInt("court_division_id"),
            courtDivision = courtDivision,
        )
    }
}