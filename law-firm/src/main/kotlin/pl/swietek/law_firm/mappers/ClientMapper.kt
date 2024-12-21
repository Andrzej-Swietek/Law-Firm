package pl.swietek.law_firm.mappers

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Client
import pl.swietek.law_firm.models.ContactDetails
import java.sql.ResultSet

@Service
class ClientMapper : RowMapper<Client> {

    override fun mapRow(rs: ResultSet, rowNum: Int): Client {
        val contactDetails = if (rs.getObject("cd_id") != null) {
            ContactDetails(
                id = rs.getInt("cd_id"),
                phoneNumber = rs.getString("phone_number"),
                email = rs.getString("cd_email"),
                street = rs.getString("street"),
                city = rs.getString("city"),
                state = rs.getString("state"),
                zipCode = rs.getString("zip_code"),
                country = rs.getString("country")
            )
        } else {
            null
        }

        return Client(
            id = rs.getInt("id"),
            firstName = rs.getString("first_name"),
            lastName = rs.getString("last_name"),
            email = rs.getString("email"),
            contactDetailsId =  rs.getInt("contact_data_id"),
            contactDetails = contactDetails
        )
    }

}

@Service
class ContactDataRowMapper : RowMapper<ContactDetails> {
    override fun mapRow(rs: ResultSet, rowNum: Int): ContactDetails {
        return ContactDetails(
            id = rs.getInt("id"),
            phoneNumber = rs.getString("phone_number"),
            email = rs.getString("email"),
            street = rs.getString("street"),
            city = rs.getString("city"),
            state = rs.getString("state"),
            zipCode = rs.getString("zip_code"),
            country = rs.getString("country")
        )
    }
}