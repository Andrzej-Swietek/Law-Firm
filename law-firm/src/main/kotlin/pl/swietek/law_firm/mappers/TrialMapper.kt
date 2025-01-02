package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.*
import java.sql.ResultSet


@Service
class TrialMapper(
    private val clientMapper: ClientMapper,
    private val lawyerMapper: LawyerMapper,
    private val judgeMapper: JudgeMapper,
    private val caseMapper: CaseMapper,
    private val trialStatusMapper: TrialStatusMapper
) : RowMapper<Trial> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Trial {
        // Mapowanie klienta
        val client = if (rs.getInt("client_id") != 0) {
            Client(
                id = rs.getInt("client_id"),
                firstName = rs.getString("client_first_name"),
                lastName = rs.getString("client_last_name"),
                email = rs.getString("client_email"),
                contactDetailsId = rs.getInt("client_contact_data_id"),
                contactDetails = null
            )
        } else {
            null
        }

        // Mapowanie prawnika
        val lawyer = if (rs.getInt("lawyer_id") != 0) {
            Lawyer(
                id = rs.getInt("lawyer_id"),
                firstName = rs.getString("lawyer_first_name"),
                lastName = rs.getString("lawyer_last_name"),
                specialization = rs.getString("lawyer_specialization") // Dodaj w widoku, jeśli brak
            )
        } else {
            null
        }

        // Mapowanie sędziego
        val judge = if (rs.getInt("judge_id") != 0) {
            Judge(
                id = rs.getInt("judge_id"),
                firstName = rs.getString("judge_first_name"),
                lastName = rs.getString("judge_last_name"),
                courtDivisionId = rs.getInt("judge_court_division_id"),
                courtDivision = CourtDivision(
                    rs.getInt("judge_court_division_id"),
                    rs.getString("court_division_name"),
                    rs.getString("court_division_city"),
                )
            )
        } else {
            null
        }

        // Mapowanie sprawy
        val case = if (rs.getInt("case_id") != 0) {
            Case(
                id = rs.getInt("case_id"),
                name = rs.getString("case_name"),
                description = rs.getString("case_description"),
                responsibleLawyerId = rs.getInt("case_responsible_lawyer_id"),
                clientId = rs.getInt("case_client_id"),
                responsibleLawyer = null,
                client = null,
                documents = emptyList()
            )
        } else {
            null
        }

        return Trial(
            id = rs.getInt("trial_id"),
            title = rs.getString("trial_title"),
            description = rs.getString("trial_description"),
            date = rs.getDate("trial_date").toLocalDate(),
            trialStatusId = rs.getInt("trial_status_id"),
            clientId = rs.getInt("trial_client_id"),
            lawyerId = rs.getInt("trial_lawyer_id"),
            judgeId = rs.getInt("trial_judge_id"),
            caseId = rs.getInt("trial_case_id"),
            trialStatus = TrialStatus(
                id = rs.getLong("trial_status_id"),
                name = rs.getString("trial_status_name")
            ),
            client = client,
            lawyer = lawyer,
            judge = judge,
            case = case
        )
    }

    fun mapBriefTrial(rs: ResultSet): Trial {
        return Trial(
            id = rs.getInt("trial_id"),
            title = rs.getString("trial_title"),
            description = rs.getString("trial_description"),
            date = rs.getDate("trial_date").toLocalDate(),
            trialStatusId = rs.getInt("trial_status_id"),
            clientId = rs.getInt("trial_client_id"),
            lawyerId = rs.getInt("trial_lawyer_id"),
            judgeId = rs.getInt("trial_judge_id"),
            caseId = rs.getInt("trial_case_id"),
            trialStatus = TrialStatus(
                id = rs.getLong("trial_status_id"),
                name = rs.getString("trial_status_name")
            ),
        )
    }

    fun mapMinimalTrial(rs: ResultSet): Trial {
        return Trial(
            id = rs.getInt("trial_id"),
            title = rs.getString("trial_title"),
            description = rs.getString("trial_description"),
            date = rs.getDate("trial_date").toLocalDate(),
            trialStatusId = rs.getInt("trial_status_id"),
            clientId = rs.getInt("trial_client_id"),
            lawyerId = rs.getInt("trial_lawyer_id"),
            judgeId = rs.getInt("trial_judge_id"),
            caseId = rs.getInt("trial_case_id"),
        )
    }
}