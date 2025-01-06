package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.models.Client
import pl.swietek.law_firm.models.ClientPayment
import java.time.LocalDate

@Repository
class ReportRepository(private val jdbcTemplate: JdbcTemplate) {
    fun getCaseCountPerLawyer(minCases: Int = 0): List<Pair<String, Int>> {
        val sql = "SELECT * FROM LawFirm.get_case_count_per_lawyer(?)"
        return jdbcTemplate.query(sql, { rs, _ ->
            rs.getString("lawyer_name") to rs.getInt("case_count")
        }, minCases)
    }

    fun getTrialDetails(): List<Map<String, Any>> {
        val sql = "SELECT * FROM LawFirm.get_trial_details()"
        return jdbcTemplate.query(sql) { rs, _ ->
            mapOf(
                "trial_title" to rs.getString("trial_title"),
                "trial_date" to rs.getDate("trial_date").toLocalDate(),
                "status" to rs.getString("status"),
                "required_documents_count" to rs.getInt("required_documents_count")
            )
        }
    }

    fun getTopClients(limitCount: Int = 10): List<Pair<String, Int>> {
        val sql = "SELECT * FROM LawFirm.get_top_clients(?)"
        return jdbcTemplate.query(sql, { rs, _ ->
            rs.getString("client_name") to rs.getInt("trial_count")
        }, limitCount)
    }

    fun getUnsignedDocuments(): List<Pair<String, Int>> {
        val sql = "SELECT * FROM LawFirm.get_unsigned_documents()"
        return jdbcTemplate.query(sql, { rs, _ ->
            rs.getString("document_title") to rs.getInt("signatures_count")
        })
    }

    fun getCaseDecisions(startDate: LocalDate = LocalDate.of(2024, 1, 1)): List<Map<String, Any>> {
        val sql = "SELECT * FROM LawFirm.get_case_decisions(?)"
        return jdbcTemplate.query(sql, { rs, _ ->
            mapOf(
                "case_name" to rs.getString("case_name"),
                "decision_name" to rs.getString("decision_name"),
                "decision_description" to rs.getString("decision_description"),
                "decision_date" to rs.getDate("decision_date").toLocalDate()
            )
        }, startDate)
    }

    fun calculateMonthlyPayment(clientId: Int): Double {
        val sql = "SELECT LawFirm.calculate_monthly_payment(?)"
        return jdbcTemplate.queryForObject(sql, Double::class.java, clientId)
            ?: throw IllegalStateException("Unable to calculate payment for client with ID: $clientId")
    }

    fun calculateMonthlyPaymentForAllClients(): List<ClientPayment> {
        val sql = """
            SELECT
                c.id, first_name, last_name, email, contact_data_id,
                (
                    CASE
                        WHEN LawFirm.calculate_monthly_payment(c.id) IS NOT NULL
                            THEN LawFirm.calculate_monthly_payment(c.id)
                        ELSE
                            0.0
                    END
                ) as payment

            FROM lawFirm.client c;
        """.trimIndent()

        return jdbcTemplate.query(sql, {
            rs, _ ->
            ClientPayment(
                id = rs.getInt("id"),
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                email = rs.getString("email"),
                contactDataId = rs.getInt("contact_data_id"),
                payment = rs.getDouble("payment")
            )
        })
            ?: throw IllegalStateException("Unable to calculate payment for all clients ")
    }

}