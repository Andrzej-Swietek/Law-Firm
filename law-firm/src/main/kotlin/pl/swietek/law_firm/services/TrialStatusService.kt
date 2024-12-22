package pl.swietek.law_firm.services

import pl.swietek.law_firm.models.TrialStatus

interface TrialStatusService {

    fun getAllTrialStatuses(): List<TrialStatus>

    fun getTrialStatusById(statusId: Long): TrialStatus?

    fun saveTrialStatus(trialStatus: TrialStatus): TrialStatus

    fun updateTrialStatus(trialStatus: TrialStatus): TrialStatus

    fun deleteTrialStatus(statusId: Int): Boolean

}