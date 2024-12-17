package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.JudgeMapper
import pl.swietek.law_firm.models.Judge

@Repository
class JudgeRepository(private val jdbcTemplate: JdbcTemplate) {

    private val judgeRowMapper = JudgeMapper()

    fun getJudges(): List<Judge> {
        val sql = """
            SELECT 
                j.id AS id, 
                j.first_name AS first_name, 
                j.last_name AS last_name, 
                cd.id AS court_division_id, 
                cd.name AS court_division_name, 
                cd.city AS court_division_city
            FROM LawFirm.judge j
            LEFT JOIN LawFirm.court_division cd ON j.court_division_id = cd.id
        """.trimIndent()

        return jdbcTemplate.query(sql, judgeRowMapper)
    }

    fun getJudgeById(judgeId: Int): Judge? {
        val sql = """
            SELECT 
                j.id AS id, 
                j.first_name AS first_name, 
                j.last_name AS last_name, 
                cd.id AS court_division_id, 
                cd.name AS court_division_name, 
                cd.city AS court_division_city
            FROM LawFirm.judge j
            LEFT JOIN LawFirm.court_division cd ON j.court_division_id = cd.id
            WHERE j.id = ?
        """.trimIndent()

        return jdbcTemplate.query(sql, judgeRowMapper, judgeId).firstOrNull()
    }

    fun saveJudge(judge: Judge): Judge {
        val sql = """
            INSERT INTO LawFirm.judge (id, first_name, last_name, court_division_id)
            VALUES (?, ?, ?, ?)
        """.trimIndent()

        jdbcTemplate.update(
            sql,
            judge.id,
            judge.firstName,
            judge.lastName,
            judge.courtDivision?.id
        )
        return judge
    }

    fun updateJudge(judge: Judge): Judge {
        val sql = """
            UPDATE LawFirm.judge
            SET first_name = ?, last_name = ?, court_division_id = ?
            WHERE id = ?
        """.trimIndent()

        jdbcTemplate.update(
            sql,
            judge.firstName,
            judge.lastName,
            judge.courtDivision?.id,
            judge.id
        )
        return judge
    }

    fun deleteJudge(judgeId: Int): Boolean {
        val sql = """
            DELETE FROM LawFirm.judge
            WHERE id = ?
        """.trimIndent()

        val rowsAffected = jdbcTemplate.update(sql, judgeId)
        return rowsAffected > 0
    }
}
