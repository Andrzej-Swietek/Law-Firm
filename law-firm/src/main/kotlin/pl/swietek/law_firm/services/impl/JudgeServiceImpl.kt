package pl.swietek.law_firm.services.impl

import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Judge
import pl.swietek.law_firm.repositories.JudgeRepository
import pl.swietek.law_firm.requests.JudgeRequest
import pl.swietek.law_firm.services.JudgeService

@Service
class JudgeServiceImpl(private val judgeRepository: JudgeRepository) : JudgeService {

    override fun getAllJudges(): List<Judge> = judgeRepository.getJudges()

    override fun getJudgeById(judgeId: Int): Judge? = judgeRepository.getJudgeById(judgeId)

    override fun saveJudge(judge: JudgeRequest): Judge = judgeRepository.saveJudge(judge)

    override fun updateJudge(judge: JudgeRequest): Judge = judgeRepository.updateJudge(judge)

    override fun deleteJudge(judgeId: Int): Boolean = judgeRepository.deleteJudge(judgeId)

}