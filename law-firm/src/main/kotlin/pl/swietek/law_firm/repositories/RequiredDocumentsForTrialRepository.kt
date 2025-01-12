package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.*
import pl.swietek.law_firm.models.Case
import pl.swietek.law_firm.models.Client
import pl.swietek.law_firm.models.RequiredDocumentForTrial
import pl.swietek.law_firm.repositories.queryBuilders.RequiredDocumentQueryBuilder
import pl.swietek.law_firm.requests.RequiredDocumentRequest
import java.sql.Connection
import java.sql.PreparedStatement

@Repository
class RequiredDocumentsForTrialRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val requiredDocumentRowMapper: RequiredDocumentForTrialMapper,
    private val documentRowMapper: DocumentMapper,
    private val trialRowMapper: TrialMapper,
    private val caseMapper: CaseMapper,
    private val lawyerMapper: LawyerMapper,
    private val judgeMapper: JudgeMapper
) {

    fun getRequiredDocumentsForTrialWithAllRelations(): List<RequiredDocumentForTrial> {
        val sql = """
            SELECT 
                rdft.id AS required_document_id, 
                rdft.trial_id AS rdft_trial_id, 
                rdft.document_id AS rdft_document_id,
                -- Trial fields
                t.id AS trial_id, 
                t.title AS trial_title, 
                t.description AS trial_description, 
                t.trial_status_id AS trial_status_id, 
                t.client_id AS trial_client_id, 
                t.lawyer_id AS trial_lawyer_id, 
                t.judge_id AS trial_judge_id, 
                t.date AS trial_date, 
                t.case_id AS trial_case_id,
                 -- Trial Status
                ts.id AS type_id,
                ts.name as trial_status_name,
                -- Document fields
                d.id AS document_id, 
                d.type_id AS document_type_id, 
                d.file_path AS file_path,
                d.title AS title,
                d.description AS description,
                dt.name AS document_type_name,
                --- Client fields
                c.id AS client_id,
                c.first_name AS client_first_name,
                c.last_name AS client_last_name,
                c.email AS client_email,
                c.contact_data_id AS client_contact_data_id
            FROM LawFirm.required_documents_for_trial rdft
            LEFT JOIN LawFirm.trial t ON rdft.trial_id = t.id
            LEFT JOIN LawFirm.document d ON rdft.document_id = d.id
            LEFT JOIN LawFirm.client c ON t.client_id = c.id
            LEFT JOIN LawFirm.trial_status ts ON  t.trial_status_id = ts.id
            LEFT JOIN LawFirm.document_types dt ON d.type_id = dt.id
        """.trimIndent()

        return jdbcTemplate.query(sql) { rs, _ ->
            val requiredDocument = requiredDocumentRowMapper.mapRow(rs, 1)
            val trial = trialRowMapper.mapBriefTrial(rs)
            val document = documentRowMapper.mapRow(rs, 1)

            requiredDocument.copy(
                trial = trial,
                document = document
            )
        }
    }

    fun getRequiredDocumentForTrialById(requiredDocumentId: Int): RequiredDocumentForTrial? {

        val query = RequiredDocumentQueryBuilder()
            .selectBasic()
            .withTrial()
            .withDocument()
            .build()

        val sql = """
            $query
            WHERE rdft.id = ?
        """.trimIndent()

        println(sql)


        return jdbcTemplate.query(sql, { rs, _ ->
            val requiredDocument = requiredDocumentRowMapper.mapRow(rs, 1)
            val trial = trialRowMapper.mapBriefTrial(rs)
            val document = documentRowMapper.mapRow(rs, 1)
            val client = Client(
                rs.getInt("client_id"),
                rs.getString("client_first_name"),
                rs.getString("client_last_name"),
                rs.getString("client_email"),
                rs.getInt("client_contact_data_id")
            )

            val case = caseMapper.mapBriefCase(rs, "case_") ?: null
            val lawyer  = lawyerMapper.mapBriefLawyer(rs , "lawyer_") ?: null
            val judge = judgeMapper.mapBriefJudge(rs, "judge_") ?: null

            requiredDocument.copy(
                trial = trial.copy(
                    client = client,
                    case = case,
                    lawyer = lawyer,
                    judge = judge,
                ),
                document = document
            )
        }, requiredDocumentId).firstOrNull()
    }

    fun getRequiredDocumentsByTrialId(trialId: Int): List<RequiredDocumentForTrial> {
        val query = RequiredDocumentQueryBuilder()
            .selectBasic()
            .withTrial()
            .withDocument()
            .build()

        val sql = """
            $query
            WHERE rdft.trial_id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, { rs, _ ->
            val requiredDocument = requiredDocumentRowMapper.mapRow(rs, 1)
            val trial = trialRowMapper.mapBriefTrial(rs)
            val document = documentRowMapper.mapRow(rs, 1)
            val client = Client(
                rs.getInt("client_id"),
                rs.getString("client_first_name"),
                rs.getString("client_last_name"),
                rs.getString("client_email"),
                rs.getInt("client_contact_data_id")
            )

            val case = caseMapper.mapBriefCase(rs, "case_") ?: null
            val lawyer  = lawyerMapper.mapBriefLawyer(rs , "lawyer_") ?: null
            val judge = judgeMapper.mapBriefJudge(rs, "judge_") ?: null

            requiredDocument.copy(
                trial = trial.copy(
                    client = client,
                    case = case,
                    lawyer = lawyer,
                    judge = judge,
                ),
                document = document
            )
        }, trialId)
    }

    fun saveRequiredDocumentForTrial(requiredDocumentForTrial: RequiredDocumentRequest): RequiredDocumentForTrial {
        val sql = """
            INSERT INTO LawFirm.required_documents_for_trial (trial_id, document_id)
            VALUES (?, ?)
        """.trimIndent()

        val keyHolder: KeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection: Connection ->
            val ps: PreparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(1, requiredDocumentForTrial.trialId)
            ps.setInt(2, requiredDocumentForTrial.documentId)
            ps
        }, keyHolder)

        val generatedId: Int = keyHolder.keyList
            .firstOrNull()
            ?.get("id")
            .toString().toInt()
            ?: throw Exception("Failed to retrieve generated ID")

        return RequiredDocumentForTrial(
            id = generatedId,
            trialId = requiredDocumentForTrial.trialId,
            documentId = requiredDocumentForTrial.documentId
        )
    }

    fun updateRequiredDocumentForTrial(requiredDocumentForTrial: RequiredDocumentRequest): RequiredDocumentForTrial {
        val sql = """
            UPDATE LawFirm.required_documents_for_trial
            SET trial_id = ?, document_id = ?
            WHERE id = ?
        """.trimIndent()

        val rowsUpdated = jdbcTemplate.update(sql,
            requiredDocumentForTrial.trialId,
            requiredDocumentForTrial.documentId,
            requiredDocumentForTrial.id
        )

        if (rowsUpdated == 0) {
            throw Exception("Failed to update RequiredDocumentForTrial with id: ${requiredDocumentForTrial.id}")
        }

        return RequiredDocumentForTrial(
            id = requiredDocumentForTrial.id ?: throw Exception("ID is required for update"),
            trialId = requiredDocumentForTrial.trialId,
            documentId = requiredDocumentForTrial.documentId
        )
    }

    fun deleteRequiredDocumentForTrial(requiredDocumentId: Int): Boolean {
        val sql = """
            DELETE FROM LawFirm.required_documents_for_trial
            WHERE id = ?
        """.trimIndent()
        return jdbcTemplate.update(sql, requiredDocumentId) > 0
    }

    fun getRequiredDocumentsForCase(caseId: Int): List<RequiredDocumentForTrial> {
        val query = RequiredDocumentQueryBuilder()
            .selectBasic()
            .withTrial()
            .withDocument()
            .build()

        val sql = """
            $query
            WHERE cs.id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, { rs, _ ->
            val requiredDocument = requiredDocumentRowMapper.mapRow(rs, 1)
            val trial = trialRowMapper.mapBriefTrial(rs)
            val document = documentRowMapper.mapRow(rs, 1)
            val client = Client(
                rs.getInt("client_id"),
                rs.getString("client_first_name"),
                rs.getString("client_last_name"),
                rs.getString("client_email"),
                rs.getInt("client_contact_data_id")
            )

            val case = caseMapper.mapBriefCase(rs, "case_") ?: null
            val lawyer  = lawyerMapper.mapBriefLawyer(rs , "lawyer_") ?: null
            val judge = judgeMapper.mapBriefJudge(rs, "judge_") ?: null

            requiredDocument.copy(
                trial = trial.copy(
                    client = client,
                    case = case,
                    lawyer = lawyer,
                    judge = judge,
                ),
                document = document
            )
        }, caseId)
    }

    fun getCasesForRequiredDocument(documentId: Int): List<Case> {
        val sql = """
            SELECT
                c.id AS case_id,
                c.name AS case_name,
                c.description AS case_description,
                c.responsible_lawyer_id AS case_responsible_lawyer_id,
                c.client_id AS case_client_id
            FROM LawFirm.required_documents_for_trial rdft
                INNER JOIN LawFirm.document d on rdft.document_id = d.id
                INNER JOIN LawFirm.trial t ON rdft.trial_id = t.id
                INNER JOIN LawFirm.case c ON t.case_id = c.id
            WHERE d.id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, { rs, _ ->
            caseMapper.mapBriefCase(rs, "case_")
        }, documentId)
    }
}
