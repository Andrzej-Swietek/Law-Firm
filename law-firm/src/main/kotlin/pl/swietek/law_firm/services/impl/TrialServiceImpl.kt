package pl.swietek.law_firm.services.impl

import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Trial
import pl.swietek.law_firm.repositories.TrialRepository
import pl.swietek.law_firm.services.TrialService

@Service
class TrialServiceImpl (private val trialRepository: TrialRepository): TrialService {
    override fun getTrials(): List<Trial> {
        return trialRepository.getTrials()
    }

    override fun getAllTrials(page: Int, size: Int): List<Trial> {
        if (page < 1 || size < 1) {
            throw IllegalArgumentException("Page and size must be greater than 0")
        }
        return trialRepository.getAllTrials(page, size)
    }

    override fun getTrialById(trialId: Int): Trial? {
        return trialRepository.getTrialById(trialId)
    }

    override fun saveTrial(trial: Trial): Trial {
        return trialRepository.saveTrial(trial)
    }

    override fun updateTrial(trial: Trial): Trial {
        return trialRepository.updateTrial(trial)
    }

    override fun deleteTrial(trialId: Int): Boolean {
        return trialRepository.deleteTrial(trialId)
    }
}