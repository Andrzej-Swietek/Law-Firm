package pl.swietek.law_firm.services.impl

import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Appeal
import pl.swietek.law_firm.repositories.AppealRepository
import pl.swietek.law_firm.services.AppealService

@Service
class AppealServiceImpl(private val appealRepository: AppealRepository) : AppealService {
    override fun getAllAppeals(page: Int, size: Int): List<Appeal> {
        if (page < 1 || size < 1) {
            throw IllegalArgumentException("Page and size must be greater than 0")
        }
        return appealRepository.getAllAppeals(page, size)
    }

    override fun getAppealById(appealId: Int): Appeal? {
        return appealRepository.getAppealById(appealId)
    }

    override fun saveAppeal(appeal: Appeal): Appeal {
        return appealRepository.saveAppeal(appeal)
    }

    override fun updateAppeal(appeal: Appeal): Appeal {
        return appealRepository.updateAppeal(appeal)
    }

    override fun deleteAppeal(appealId: Int): Boolean {
        return appealRepository.deleteAppeal(appealId)
    }
}