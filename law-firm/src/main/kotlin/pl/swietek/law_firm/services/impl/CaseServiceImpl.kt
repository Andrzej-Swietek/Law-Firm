package pl.swietek.law_firm.services.impl

import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Case
import pl.swietek.law_firm.repositories.CaseRepository
import pl.swietek.law_firm.services.CaseService

@Service
class CaseServiceImpl(
    private val caseRepository: CaseRepository
): CaseService {

    override fun getAllCases(page: Int, size: Int): List<Case> {
        return caseRepository.getAllCases(page, size)
    }

    override fun getCaseById(caseId: Int): Case? {
        return caseRepository.getCaseById(caseId)
    }

    override fun getCasesByLawyerId(lawyerId: Int, page: Int, size: Int): List<Case> {
        return caseRepository.getCasesByLawyerId(lawyerId, page, size)
    }

    override fun getCasesByClientId(clientId: Int, page: Int, size: Int): List<Case> {
        return caseRepository.getCasesByClientId(clientId, page, size)
    }

    override fun saveCase(newCase: Case): Case {
        return caseRepository.saveCase(newCase)
    }

    override fun updateCase(updatedCase: Case): Case {
        return caseRepository.updateCase(updatedCase)
    }

    override fun deleteCase(caseId: Int): Boolean {
        return caseRepository.deleteCase(caseId)
    }
}