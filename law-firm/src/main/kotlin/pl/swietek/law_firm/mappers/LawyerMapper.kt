package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Lawyer
import java.sql.ResultSet

@Service
class LawyerMapper : RowMapper<Lawyer> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Lawyer {
        return Lawyer(
            id = rs.getInt("id"),
            firstName = rs.getString("first_name"),
            lastName = rs.getString("last_name"),
            specialization = rs.getString("specialization")
        )
    }

    fun mapBriefLawyer(rs: ResultSet, prefix: String =""): Lawyer {
        return Lawyer(
            id = rs.getInt("${prefix}id"),
            firstName = rs.getString("${prefix}first_name"),
            lastName = rs.getString("${prefix}last_name"),
            specialization = rs.getString("${prefix}specialization")
        )
    }
}