package pl.swietek.law_firm.services

import pl.swietek.law_firm.models.ClientPayment
import java.time.LocalDate

interface ReportService {
    fun generateLawyerCaseReport(minCases: Int): List<Pair<String, Int>>

    fun fetchTrialDetails(): List<Map<String, Any>>

    fun getTopClients(limit: Int): List<Pair<String, Int>>

    fun findUnsignedDocuments(): List<Pair<String, Int>>

    fun listCaseDecisions(fromDate: LocalDate): List<Map<String, Any>>

    fun calculateMonthlyPayment(clientId: Int): Double

    fun calculateMonthlyPaymentForAllClients(): List<ClientPayment>
}