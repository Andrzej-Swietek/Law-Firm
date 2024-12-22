package pl.swietek.law_firm.services

import pl.swietek.law_firm.models.Judge
import pl.swietek.law_firm.requests.JudgeRequest

interface JudgeService {

    fun getAllJudges(): List<Judge>

    fun getJudgeById(judgeId: Int): Judge?

    fun saveJudge(judge: JudgeRequest): Judge

    fun updateJudge(judge: JudgeRequest): Judge

    fun deleteJudge(judgeId: Int): Boolean

}