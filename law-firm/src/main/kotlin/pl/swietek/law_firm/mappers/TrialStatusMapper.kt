package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.TrialStatus
import java.sql.ResultSet

@Service
class TrialStatusMapper : RowMapper<TrialStatus> {
    override fun mapRow(rs: ResultSet, rowNum: Int): TrialStatus {
        return TrialStatus(
            id = rs.getLong("id"),
            name = rs.getString("name")
        )
    }
}
