package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Appeal
import pl.swietek.law_firm.models.Trial
import java.sql.ResultSet

@Service
class AppealMapper(
    private val rulingMapper: RulingMapper,
    private val trialMapper: TrialMapper,
    private val clientMapper: ClientMapper,
    private val lawyerMapper: LawyerMapper
) : RowMapper<Appeal> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Appeal {
        val appeal =  Appeal(
            id = rs.getInt("appeal_id"),
            initialRulingId = rs.getInt("appeal_initial_ruling_id"),
            finalRulingId = rs.getInt("appeal_final_ruling_id"),
            trialId = rs.getInt("appeal_trial_id"),

            initialRuling = rulingMapper.mapRowFromPrefix(rs, "appeal_initial_"),
            finalRuling = rulingMapper.mapRowFromPrefix(rs, "appeal_final_"),
            trial = trialMapper.mapRow(rs, rowNum),
//            trial = Trial(
//                id = rs.getInt("trial_id"),
//                title = rs.getString("trial_title"),
//                description = rs.getString("trial_description"),
//                trialStatusId = rs.getInt("trial_status_id"),
//                clientId = rs.getInt("trial_client_id"),
//                lawyerId = rs.getInt("trial_lawyer_id"),
//                judgeId =rs.getInt("trial_judge_id"),
//                date = rs.getDate("trial_date").toLocalDate(),
//                caseId = rs.getInt("trial_case_id"),
//
//                client = clientMapper.mapRow(rs, rowNum),
//                lawyer = lawyerMapper.mapRow(rs, rowNum),
//                judge = null,
//                case = null
//            )
        )

        return appeal
    }
}