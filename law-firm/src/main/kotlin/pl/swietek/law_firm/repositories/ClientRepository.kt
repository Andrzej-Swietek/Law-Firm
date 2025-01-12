package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.ClientMapper
import pl.swietek.law_firm.models.Client

@Repository
class ClientRepository(private val jdbcTemplate: JdbcTemplate) {

    private val clientRowMapper = ClientMapper()

    fun getClients(page: Int, size: Int): List<Client> {
        val offset = (page - 1) * size
        val sql = """
            SELECT 
                cl.*, 
                cd.id AS cd_id, 
                cd.phone_number, 
                cd.email AS cd_email, 
                cd.street, 
                cd.city, 
                cd.state, 
                cd.zip_code, 
                cd.country
            FROM LawFirm.client cl
            LEFT JOIN LawFirm.contact_data cd ON cl.contact_data_id = cd.id
            LIMIT ? OFFSET ?
        """.trimIndent()

        return jdbcTemplate.query(sql, clientRowMapper, size, offset)
    }

    fun getClientById(clientId: Long): Client? {
        val sql = """
            SELECT 
                cl.*, 
                cd.id AS cd_id, 
                cd.phone_number, 
                cd.email AS cd_email, 
                cd.street, 
                cd.city, 
                cd.state, 
                cd.zip_code, 
                cd.country
            FROM LawFirm.client cl
            LEFT JOIN LawFirm.contact_data cd ON cl.contact_data_id = cd.id
            WHERE cl.id = ?
        """.trimIndent()

        return jdbcTemplate
            .query(sql, clientRowMapper, clientId)
            .firstOrNull()
    }

    fun getClientByEmail(email: String): Client? {
        val sql = """
            SELECT 
                cl.*, 
                cd.id AS cd_id, 
                cd.phone_number, 
                cd.email AS cd_email, 
                cd.street, 
                cd.city, 
                cd.state, 
                cd.zip_code, 
                cd.country
            FROM LawFirm.client cl
            LEFT JOIN LawFirm.contact_data cd ON cl.contact_data_id = cd.id
            WHERE cl.email = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, clientRowMapper, email).firstOrNull()
    }

    fun getClientByLawyerId(lawyerId: Int): List<Client> {
        val sql = """
            SELECT DISTINCT 
                cl.*, 
                cd.id AS cd_id, 
                cd.phone_number, 
                cd.email AS cd_email, 
                cd.street, 
                cd.city, 
                cd.state, 
                cd.zip_code, 
                cd.country
            FROM LawFirm.client cl
                LEFT JOIN LawFirm.contact_data cd ON cl.contact_data_id = cd.id
            WHERE cl.id IN (
                -- Klienci związani z tabelą case, gdzie prawnik jest odpowiedzialny
                SELECT c.client_id
                FROM LawFirm.case c
                    WHERE c.responsible_lawyer_id = ?
                UNION
                -- Klienci związani z tabelą trial, gdzie prawnik uczestniczy w rozprawie
                SELECT t.client_id
                FROM LawFirm.trial t
                    WHERE t.lawyer_id = ?
            )
        """.trimIndent()

        return jdbcTemplate.query(sql, clientRowMapper, lawyerId, lawyerId)
    }

    fun saveClient(client: Client): Client {
        val sql = """
            INSERT INTO LawFirm.client (first_name, last_name, email, contact_data_id)
            VALUES (?, ?, ?, ?)
        """.trimIndent()

        println(client.contactDetailsId)
        println("Executing SQL: $sql")

        jdbcTemplate.update(
            sql,
            client.firstName,
            client.lastName,
            client.email,
            client.contactDetailsId
        )

        return client
    }

    fun updateClient(client: Client): Client {
        val sql = """
            UPDATE LawFirm.client 
            SET first_name = ?, last_name = ?, email = ?, contact_data_id = ?
            WHERE id = ?
        """.trimIndent()

        jdbcTemplate.update(
            sql,
            client.firstName,
            client.lastName,
            client.email,
            client.contactDetailsId,
            client.id
        )

        return client
    }

    fun deleteClient(clientId: Int): Boolean {
        val sql = "DELETE FROM LawFirm.client WHERE id = ?"
        val rowsAffected = jdbcTemplate.update(sql, clientId)
        return rowsAffected > 0
    }
}
