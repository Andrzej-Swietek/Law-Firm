package pl.swietek.law_firm.services.impl

import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.ClientPayment
import pl.swietek.law_firm.repositories.ReportRepository
import pl.swietek.law_firm.services.ReportService
import java.time.LocalDate

@Service
class ReportServiceImpl(private val reportRepository: ReportRepository): ReportService {

    override fun generateLawyerCaseReport(minCases: Int): List<Pair<String, Int>> {
        return reportRepository.getCaseCountPerLawyer(minCases)
    }

    override fun fetchTrialDetails(): List<Map<String, Any>> {
        return reportRepository.getTrialDetails()
    }

    override fun getTopClients(limit: Int): List<Pair<String, Int>> {
        return reportRepository.getTopClients(limit)
    }

    override fun findUnsignedDocuments(): List<Pair<String, Int>> {
        return reportRepository.getUnsignedDocuments()
    }

    override fun listCaseDecisions(fromDate: LocalDate): List<Map<String, Any>> {
        return reportRepository.getCaseDecisions(fromDate)
    }

    override fun calculateMonthlyPayment(clientId: Int): Double {
        return reportRepository.calculateMonthlyPayment(clientId)
    }

    override fun calculateMonthlyPaymentForAllClients(): List<ClientPayment> {
        return reportRepository
            .calculateMonthlyPaymentForAllClients()
    }
}