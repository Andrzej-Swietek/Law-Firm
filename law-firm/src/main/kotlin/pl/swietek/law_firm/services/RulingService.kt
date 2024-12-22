package pl.swietek.law_firm.services

import pl.swietek.law_firm.models.Ruling

interface RulingService {

    fun getRulingById(rulingId: Int): Ruling?

    fun getAllRulings(page: Int, size: Int): List<Ruling>

    fun saveRuling(newRuling: Ruling): Ruling

    fun updateRuling(updatedRuling: Ruling): Ruling

    fun deleteRuling(rulingId: Int): Boolean
}