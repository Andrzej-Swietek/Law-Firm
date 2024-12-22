package pl.swietek.law_firm.services

import pl.swietek.law_firm.models.Appeal

interface AppealService {

    fun getAllAppeals(page: Int, size: Int): List<Appeal>

    fun getAppealById(appealId: Int): Appeal?

    fun saveAppeal(appeal: Appeal): Appeal

    fun updateAppeal(appeal: Appeal): Appeal

    fun deleteAppeal(appealId: Int): Boolean
}