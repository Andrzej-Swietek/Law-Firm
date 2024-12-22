package pl.swietek.law_firm.services.impl

import org.springframework.stereotype.Service
import pl.swietek.law_firm.exceptions.CourtNotFoundException
import pl.swietek.law_firm.models.CourtDivision
import pl.swietek.law_firm.repositories.CourtDivisionRepository
import pl.swietek.law_firm.services.CourtService

@Service
class CourtServiceImpl(
    private val courtDivisionRepository: CourtDivisionRepository
) : CourtService {
    override fun getAllCourts(): List<CourtDivision> {
        return courtDivisionRepository.getAllCourtDivisions()
    }

    override fun saveCourt(court: CourtDivision): CourtDivision {
        return this.courtDivisionRepository.saveCourtDivision(court);
    }

    override fun getCourtById(id: Int): CourtDivision? {
        return this.courtDivisionRepository.getCourtDivisionById(id.toLong())
    }

    override fun updateCourt(id: Int, updatedCourt: CourtDivision): CourtDivision {
        val existingCourt = courtDivisionRepository.getCourtDivisionById(id.toLong())
            ?: throw CourtNotFoundException("Court with ID $id not found")
        val mergedCourt = existingCourt.copy(
            name = updatedCourt.name,
            city = updatedCourt.city,
        )
        return this.saveCourt(mergedCourt)
    }

    override fun deleteCourt(id: Int) {}
}