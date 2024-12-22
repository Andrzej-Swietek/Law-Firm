package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import pl.swietek.law_firm.mappers.ContactDataRowMapper
import pl.swietek.law_firm.models.ContactDetails
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Statement

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


//    fun saveContactData(contactData: ContactDetails): ContactDetails {
//        val sql = """
//            INSERT INTO LawFirm.contact_data(phone_number, email, street, city, state, zip_code, country)
//            VALUES (?, ?, ?, ?, ?, ?, ?)
//        """.trimIndent()
//
//        val generatedId = jdbcTemplate.update(
//            sql,
//            contactData.phoneNumber,
//            contactData.email,
//            contactData.street,
//            contactData.city,
//            contactData.state,
//            contactData.zipCode,
//            contactData.country
//        )
//
//        return contactData.copy(id = generatedId)
//    }

    fun saveContactData(contactData: ContactDetails): ContactDetails {
        val sql = """
            INSERT INTO LawFirm.contact_data(phone_number, email, street, city, state, zip_code, country)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

        val keyHolder: KeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection: Connection ->
            val ps: PreparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setString(1, contactData.phoneNumber)
            ps.setString(2, contactData.email)
            ps.setString(3, contactData.street)
            ps.setString(4, contactData.city)
            ps.setString(5, contactData.state)
            ps.setString(6, contactData.zipCode)
            ps.setString(7, contactData.country)
            ps
        }, keyHolder)

        val generatedId: Int = keyHolder.keyList
            .firstOrNull()
            ?.get("id")
            .toString().toInt()
            ?: throw Exception("Failed to retrieve generated ID")

        return contactData.copy(id = generatedId)
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
