package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.DocumentMapper
import pl.swietek.law_firm.mappers.RequiredDocumentForTrialMapper
import pl.swietek.law_firm.mappers.TrialMapper
import pl.swietek.law_firm.models.RequiredDocumentForTrial

@Repository
class RequiredDocumentsForTrialRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val requiredDocumentRowMapper: RequiredDocumentForTrialMapper,
    private val documentRowMapper: DocumentMapper,
    private val trialRowMapper: TrialMapper
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
                -- Document fields
                d.id AS document_id, 
                d.type_id AS document_type_id, 
                d.file_path AS document_file_path
            FROM LawFirm.required_documents_for_trial rdft
            LEFT JOIN LawFirm.trial t ON rdft.trial_id = t.id
            LEFT JOIN LawFirm.document d ON rdft.document_id = d.id
        """.trimIndent()

        return jdbcTemplate.query(sql) { rs, _ ->
            val requiredDocument = requiredDocumentRowMapper.mapRow(rs, 1)
            val trial = trialRowMapper.mapRow(rs, 1)
            val document = documentRowMapper.mapRow(rs, 1)

            requiredDocument.copy(
                trial = trial,
                document = document
            )
        }
    }

    fun getRequiredDocumentForTrialById(requiredDocumentId: Int): RequiredDocumentForTrial? {
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
                -- Document fields
                d.id AS document_id, 
                d.type_id AS document_type_id, 
                d.file_path AS document_file_path
            FROM LawFirm.required_documents_for_trial rdft
            LEFT JOIN LawFirm.trial t ON rdft.trial_id = t.id
            LEFT JOIN LawFirm.document d ON rdft.document_id = d.id
            WHERE rdft.id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql) { rs, _ ->
            val requiredDocument = requiredDocumentRowMapper.mapRow(rs, 1)
            val trial = trialRowMapper.mapRow(rs, 1)
            val document = documentRowMapper.mapRow(rs, 1)

            requiredDocument.copy(
                trial = trial,
                document = document
            )
        }.firstOrNull()
    }

    fun saveRequiredDocumentForTrial(requiredDocumentForTrial: RequiredDocumentForTrial): RequiredDocumentForTrial {
        val sql = """
            INSERT INTO LawFirm.required_documents_for_trial (id, trial_id, document_id)
            VALUES (null, ?, ?)
        """.trimIndent()

        jdbcTemplate.update(
            sql,
            requiredDocumentForTrial.trialId,
            requiredDocumentForTrial.documentId
        )

        return requiredDocumentForTrial
    }

    fun deleteRequiredDocumentForTrial(requiredDocumentId: Int): Boolean {
        val sql = "DELETE FROM LawFirm.required_documents_for_trial WHERE id = ?"
        return jdbcTemplate.update(sql, requiredDocumentId) > 0
    }
}
