package pl.swietek.law_firm.repositories

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import pl.swietek.law_firm.mappers.JudgeMapper
import pl.swietek.law_firm.models.Judge
import pl.swietek.law_firm.requests.JudgeRequest
import java.sql.Connection
import java.sql.PreparedStatement

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

    fun saveJudge(judge: JudgeRequest): Judge {
        val sql = """
            INSERT INTO LawFirm.judge (first_name, last_name, court_division_id)
            VALUES ( ?, ?, ?)
        """.trimIndent()

        val keyHolder: KeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ connection: Connection ->
            val ps: PreparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setString(1, judge.firstName)
            ps.setString(2, judge.lastName)
            ps.setInt(3, judge.courtDivisionId)
            ps
        }, keyHolder)

        val generatedId: Int = keyHolder.keyList
            .firstOrNull()
            ?.get("id")
            .toString().toInt()
            ?: throw Exception("Failed to retrieve generated ID")

        return this.getJudgeById(generatedId)!!
    }

    fun updateJudge(judge: JudgeRequest): Judge {
        val sql = """
            UPDATE LawFirm.judge
            SET first_name = ?, last_name = ?, court_division_id = ?
            WHERE id = ?
        """.trimIndent()

        jdbcTemplate.update(
            sql,
            judge.firstName,
            judge.lastName,
            judge.courtDivisionId,
            judge.id
        )
        return this.getJudgeById(judge.id!!)!!
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
