package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Case
import pl.swietek.law_firm.models.Client
import pl.swietek.law_firm.models.Lawyer
import java.sql.ResultSet

@Service
class CaseMapper(
    private val lawyerMapper: LawyerMapper,
    private val clientMapper: ClientMapper,
) : RowMapper<Case> {
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
}