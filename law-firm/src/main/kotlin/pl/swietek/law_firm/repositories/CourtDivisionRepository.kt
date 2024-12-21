package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.CourtDivisionMapper
import pl.swietek.law_firm.models.CourtDivision

@Repository
class CourtDivisionRepository(private val jdbcTemplate: JdbcTemplate) {

    private val courtDivisionRowMapper = CourtDivisionMapper()

    fun getAllCourtDivisions(): List<CourtDivision> {
        val sql = "SELECT * FROM LawFirm.court_division"
        return jdbcTemplate.query(sql, courtDivisionRowMapper)
    }

    fun getCourtDivisionById(divisionId: Long): CourtDivision? {
        val sql = "SELECT * FROM LawFirm.court_division WHERE id = ?"
        return jdbcTemplate.query(sql, courtDivisionRowMapper, divisionId).firstOrNull()
    }

    fun saveCourtDivision(courtDivision: CourtDivision): CourtDivision {
        val sql = "INSERT INTO LawFirm.court_division (id, name, city) VALUES (null, ?, ?)"
        jdbcTemplate.update(sql, courtDivision.name, courtDivision.city)
        return courtDivision
    }

    fun updateCourtDivision(courtDivision: CourtDivision): CourtDivision {
        val sql = "UPDATE LawFirm.court_division SET name = ?, city = ? WHERE id = ?"
        jdbcTemplate.update(sql, courtDivision.name, courtDivision.city, courtDivision.id)
        return courtDivision
    }

    fun deleteCourtDivision(divisionId: Int): Boolean {
        val sql = "DELETE FROM LawFirm.court_division WHERE id = ?"
        val rowsAffected = jdbcTemplate.update(sql, divisionId)
        return rowsAffected > 0
    }
}