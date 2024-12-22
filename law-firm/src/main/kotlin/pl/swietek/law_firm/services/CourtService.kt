package pl.swietek.law_firm.services

import pl.swietek.law_firm.models.CourtDivision

interface CourtService {
    fun getAllCourts(): List<CourtDivision>

    fun saveCourt(court: CourtDivision): CourtDivision

    fun getCourtById(id: Int): CourtDivision?

    fun updateCourt(id: Int, updatedCourt: CourtDivision): CourtDivision

    fun deleteCourt(id: Int)
}