package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.CourtMapper
import pl.swietek.law_firm.models.CourtDivision

@Repository
class CourtRepository(private val jdbcTemplate: JdbcTemplate) {

    private val courtRowMapper = CourtMapper()

    fun getCourts(): List<CourtDivision> {
        val sql = """
            SELECT id, name, city
            FROM LawFirm.court_division
        """.trimIndent()

        return jdbcTemplate.query(sql, courtRowMapper)
    }

    fun getCourtById(courtId: Int): CourtDivision? {
        val sql = """
            SELECT id, name, city
            FROM LawFirm.court_division
            WHERE id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, courtRowMapper, courtId).firstOrNull()
    }

    fun saveCourt(court: CourtDivision): CourtDivision {
        val sql = """
            INSERT INTO LawFirm.court_division (id, name, city)
            VALUES (?, ?, ?)
        """.trimIndent()

        jdbcTemplate.update(sql, court.id, court.name, court.city)
        return court
    }

    fun updateCourt(court: CourtDivision): CourtDivision {
        val sql = """
            UPDATE LawFirm.court_division 
            SET name = ?, city = ?
            WHERE id = ?
        """.trimIndent()

        jdbcTemplate.update(sql, court.name, court.city, court.id)
        return court
    }

    fun deleteCourt(courtId: Int): Boolean {
        val sql = """
            DELETE FROM LawFirm.court_division
            WHERE id = ?
        """.trimIndent()

        val rowsAffected = jdbcTemplate.update(sql, courtId)
        return rowsAffected > 0
    }
}
