package pl.swietek.law_firm.services.impl

import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Decision
import pl.swietek.law_firm.repositories.DecisionRepository
import pl.swietek.law_firm.services.DecisionService

@Service
class DecisionServiceImpl(private val decisionRepository: DecisionRepository): DecisionService {

    override fun getAllDecisions(page: Int, size: Int): List<Decision> {
        if (page < 1 || size < 1) {
            throw IllegalArgumentException("Page and size must be greater than 0")
        }
        return decisionRepository.getAllDecisions(page, size)
    }

    override fun getDecisionById(decisionId: Int): Decision? {
        return decisionRepository.getDecisionById(decisionId)
    }

    override fun saveDecision(decision: Decision): Decision {
        return decisionRepository.saveDecision(decision)
    }

    override fun updateDecision(decision: Decision): Decision {
        return decisionRepository.updateDecision(decision)
    }

    override fun deleteDecision(decisionId: Int): Boolean {
        return decisionRepository.deleteDecision(decisionId)
    }
}