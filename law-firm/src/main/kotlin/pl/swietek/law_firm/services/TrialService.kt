package pl.swietek.law_firm.services

import pl.swietek.law_firm.models.Trial
import pl.swietek.law_firm.requests.TrialRequest

interface TrialService {

    fun getTrials(): List<Trial>

    fun getAllTrials(page: Int, size: Int): List<Trial>

    fun getTrialById(trialId: Int): Trial?

    fun getTrialsByClientId(clientId: Int): List<Trial>

    fun getTrialsByLawyerId(lawyerId: Int): List<Trial>

    fun saveTrial(trial: TrialRequest): Trial

    fun updateTrial(trial: TrialRequest): Trial

    fun deleteTrial(trialId: Int): Boolean

}