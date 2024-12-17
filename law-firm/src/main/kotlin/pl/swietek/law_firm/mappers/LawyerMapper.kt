package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import pl.swietek.law_firm.models.Lawyer
import java.sql.ResultSet

class LawyerMapper : RowMapper<Lawyer> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Lawyer {
        return Lawyer(
            id = rs.getInt("id"),
            firstName = rs.getString("first_name"),
            lastName = rs.getString("last_name"),
            specialization = rs.getString("specialization")
        )
    }
}