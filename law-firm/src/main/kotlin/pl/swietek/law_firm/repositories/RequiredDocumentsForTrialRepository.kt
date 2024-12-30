package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.DocumentMapper
import pl.swietek.law_firm.mappers.RequiredDocumentForTrialMapper
import pl.swietek.law_firm.mappers.TrialMapper
import pl.swietek.law_firm.models.RequiredDocumentForTrial
import pl.swietek.law_firm.requests.RequiredDocumentRequest
import java.sql.Connection
import java.sql.PreparedStatement

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
                d.file_path AS document_file_path,
                d.title AS document_title,
                d.description AS document_description
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
                d.file_path AS document_file_path,
                d.title AS document_title,
                d.description AS document_description
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
}
