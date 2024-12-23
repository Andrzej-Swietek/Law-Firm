package pl.swietek.law_firm.services

import pl.swietek.law_firm.models.Case
import pl.swietek.law_firm.requests.CaseRequest

interface CaseService {

    fun getAllCases(page: Int, size: Int): List<Case>

    fun getCaseById(caseId: Int): Case?

    fun getCasesByLawyerId(lawyerId: Int, page: Int, size: Int): List<Case>

    fun getCasesByClientId(clientId: Int, page: Int, size: Int): List<Case>

    fun saveCase(newCase: CaseRequest): Case

    fun updateCase(updatedCase: CaseRequest): Case

    fun deleteCase(caseId: Int): Boolean
}