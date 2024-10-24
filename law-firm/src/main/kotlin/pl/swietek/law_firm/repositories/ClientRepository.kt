package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.models.Client

@Repository
class ClientRepository(private val jdbcTemplate: JdbcTemplate) {

//    fun getClients(): List<Client> {
//        return listOf()
//    }
//
//    fun getClientById(clientId: String): Client? {
//    }
//
//    fun getClientByEmail(email: String): Client? {}
//
//    fun saveClient(client: Client): Client {
//        this.jdbcTemplate.update(
//            ""
//        )
//        return client
//    }
//
//    fun updateClient(client: Client): Client {}
//
//    fun deleteClient(client: Client): Client? {}

}