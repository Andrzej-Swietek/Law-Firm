package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Client
import pl.swietek.law_firm.models.CourtDivision
import java.sql.ResultSet

@Service
class CourtMapper  : RowMapper<CourtDivision> {
    override fun mapRow(rs: ResultSet, rowNum: Int): CourtDivision? {
        return CourtDivision(
            id = rs.getInt("id"),
            name = rs.getString("name"),
            city = rs.getString("city")
        )
    }
}