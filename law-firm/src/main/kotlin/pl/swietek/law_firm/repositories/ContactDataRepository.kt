package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.ContactDataRowMapper
import pl.swietek.law_firm.models.ContactDetails

@Repository
class ContactDataRepository(private val jdbcTemplate: JdbcTemplate) {

    private val contactDataRowMapper = ContactDataRowMapper()

    fun getContactDataById(contactDataId: Long): ContactDetails? {
        val sql = """
            SELECT * 
            FROM LawFirm.contact_data 
            WHERE id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, contactDataRowMapper, contactDataId).firstOrNull()
    }

    fun saveContactData(contactData: ContactDetails): ContactDetails {
        val sql = """
            INSERT INTO LawFirm.contact_data (id, phone_number, email, street, city, state, zip_code, country) 
            VALUES (null, ?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

        jdbcTemplate.update(
            sql,
            contactData.phoneNumber,
            contactData.email,
            contactData.street,
            contactData.city,
            contactData.state,
            contactData.zipCode,
            contactData.country
        )

        return contactData
    }

    fun updateContactData(contactData: ContactDetails): ContactDetails {
        val sql = """
            UPDATE LawFirm.contact_data 
            SET phone_number = ?, email = ?, street = ?, city = ?, state = ?, zip_code = ?, country = ? 
            WHERE id = ?
        """.trimIndent()

        jdbcTemplate.update(
            sql,
            contactData.phoneNumber,
            contactData.email,
            contactData.street,
            contactData.city,
            contactData.state,
            contactData.zipCode,
            contactData.country,
            contactData.id
        )

        return contactData
    }

    fun deleteContactData(contactDataId: Int): Boolean {
        val sql = "DELETE FROM LawFirm.contact_data WHERE id = ?"
        val rowsAffected = jdbcTemplate.update(sql, contactDataId)
        return rowsAffected > 0
    }
}
