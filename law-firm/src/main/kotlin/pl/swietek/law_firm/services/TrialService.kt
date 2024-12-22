package pl.swietek.law_firm.services

import pl.swietek.law_firm.models.Trial

interface TrialService {

    fun getTrials(): List<Trial>

    fun getAllTrials(page: Int, size: Int): List<Trial>

    fun getTrialById(trialId: Int): Trial?

    fun saveTrial(trial: Trial): Trial

    fun updateTrial(trial: Trial): Trial

    fun deleteTrial(trialId: Int): Boolean

}