package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class LawyerRepository(private val jdbcTemplate: JdbcTemplate) {
}