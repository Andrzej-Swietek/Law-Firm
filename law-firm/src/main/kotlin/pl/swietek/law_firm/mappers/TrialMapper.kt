package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Document
import pl.swietek.law_firm.models.Trial
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
            trialStatus = trialStatusMapper.mapRow(rs, rowNum),
            client = clientMapper.mapRow(rs, rowNum),
            lawyer = lawyerMapper.mapRow(rs, rowNum),
            judge = judgeMapper.mapRow(rs, rowNum),
            case = caseMapper.mapRow(rs, rowNum)
        )
    }
}