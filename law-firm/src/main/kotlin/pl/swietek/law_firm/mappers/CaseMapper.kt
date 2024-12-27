package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Case
import pl.swietek.law_firm.models.Client
import pl.swietek.law_firm.models.Lawyer
import java.sql.ResultSet
import java.sql.SQLException

@Service
class CaseMapper(
    private val lawyerMapper: LawyerMapper,
    private val clientMapper: ClientMapper,
) : RowMapper<Case> {

    companion object {
        fun hasColumn(rs: ResultSet, columnName: String): Boolean {
            return try {
                rs.findColumn(columnName)
                true
            } catch (e: SQLException) {
                false
            }
        }
    }

    override fun mapRow(rs: ResultSet, rowNum: Int): Case {
        // Mapowanie klienta
        val client = if (rs.getInt("case_client_id") != 0) {
            Client(
                id = rs.getInt("case_client_id"),
                firstName = rs.getString("client_first_name"),
                lastName = rs.getString("client_last_name"),
                email = rs.getString("client_email"),
                contactDetailsId = rs.getInt("client_contact_details_id"),
                contactDetails = null
            )
        } else {
            null
        }

        // Mapowanie prawnika
        val lawyer = if (rs.getInt("case_responsible_lawyer_id") != 0) {
            Lawyer(
                id = rs.getInt("case_responsible_lawyer_id"),
                firstName = rs.getString("lawyer_first_name"),
                lastName = rs.getString("lawyer_last_name"),
                specialization = rs.getString("lawyer_specialization")
            )
        } else {
            null
        }

        return Case(
            id = rs.getInt("id"),
            name = rs.getString("name"),
            description = rs.getString("description"),
            responsibleLawyerId = rs.getInt("case_responsible_lawyer_id"),
            clientId = rs.getInt("case_client_id"),
            responsibleLawyer = lawyer,
            client = client,
            documents = emptyList()
        )
    }

    fun mapBriefCase(rs: ResultSet, prefix: String = ""): Case {
        return Case(
            id = if (hasColumn(rs, "${prefix}id")) rs.getInt("${prefix}id") else 0,
            name = if (hasColumn(rs, "${prefix}name")) rs.getString("${prefix}name") ?: "" else "",
            description = if (hasColumn(rs, "${prefix}description")) rs.getString("${prefix}description") ?: "" else "",
            responsibleLawyerId = if (hasColumn(rs, "${prefix}responsible_lawyer_id")) rs.getInt("${prefix}responsible_lawyer_id") else 0,
            clientId = if (hasColumn(rs, "${prefix}client_id")) rs.getInt("${prefix}client_id") else 0,
            responsibleLawyer = null,
            client = null,
            documents = null
        )
    }
}