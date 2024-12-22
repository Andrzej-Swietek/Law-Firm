package pl.swietek.law_firm.services

import pl.swietek.law_firm.models.Judge

interface JudgeService {

    fun getAllJudges(): List<Judge>

    fun getJudgeById(judgeId: Int): Judge?

    fun saveJudge(judge: Judge): Judge

    fun updateJudge(judge: Judge): Judge

    fun deleteJudge(judgeId: Int): Boolean

}