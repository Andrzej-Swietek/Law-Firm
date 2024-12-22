package pl.swietek.law_firm.services.impl

import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.TrialStatus
import pl.swietek.law_firm.repositories.TrialStatusRepository
import pl.swietek.law_firm.services.TrialStatusService

@Service
class TrialStatusServiceImpl(
    private val trialStatusRepository: TrialStatusRepository
): TrialStatusService {

    override fun getAllTrialStatuses(): List<TrialStatus> {
        return trialStatusRepository.getAllTrialStatuses()
    }

    override fun getTrialStatusById(statusId: Long): TrialStatus? {
        return trialStatusRepository.getTrialStatusById(statusId)
    }

    override fun saveTrialStatus(trialStatus: TrialStatus): TrialStatus {
        return trialStatusRepository.saveTrialStatus(trialStatus)
    }

    override fun updateTrialStatus(trialStatus: TrialStatus): TrialStatus {
        return trialStatusRepository.updateTrialStatus(trialStatus)
    }

    override fun deleteTrialStatus(statusId: Int): Boolean {
        return trialStatusRepository.deleteTrialStatus(statusId)
    }
}
