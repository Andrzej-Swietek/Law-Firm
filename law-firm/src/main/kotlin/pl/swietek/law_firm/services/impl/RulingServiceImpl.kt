package pl.swietek.law_firm.services.impl

import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Ruling
import pl.swietek.law_firm.repositories.RulingRepository
import pl.swietek.law_firm.services.RulingService

@Service
class RulingServiceImpl(
    private val rulingRepository: RulingRepository
) : RulingService {

    override fun getRulingById(rulingId: Int): Ruling? {
        return rulingRepository.getRulingById(rulingId)
    }

    override fun getAllRulings(page: Int, size: Int): List<Ruling> {
        return rulingRepository.getAllRulings(page, size)
    }

    override fun saveRuling(newRuling: Ruling): Ruling {
        return rulingRepository.saveRuling(newRuling)
    }

    override fun updateRuling(updatedRuling: Ruling): Ruling {
        return rulingRepository.updateRuling(updatedRuling)
    }

    override fun deleteRuling(rulingId: Int): Boolean {
        return rulingRepository.deleteRuling(rulingId)
    }
}