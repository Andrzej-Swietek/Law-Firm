package pl.swietek.law_firm.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.swietek.law_firm.models.ClientPayment
import pl.swietek.law_firm.reponses.LawyerTrialsCount
import pl.swietek.law_firm.reponses.LawyerTrialsCountResponse
import pl.swietek.law_firm.reponses.TopClient
import pl.swietek.law_firm.reponses.TopClientResponse
import pl.swietek.law_firm.services.ReportService
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/reports")
class ReportsController(private val reportService: ReportService) {

    @GetMapping("/lawyer-cases")
    fun getLawyerCases(@RequestParam minCases: Int): ResponseEntity<LawyerTrialsCountResponse> {
        val lawyersCount=  reportService.generateLawyerCaseReport(minCases).map {
            it ->  LawyerTrialsCount(it.first, it.second)
        }
        return ResponseEntity.ok(
            LawyerTrialsCountResponse(
                lawyers = lawyersCount,
                count = lawyersCount.size
            )
        )
    }

    @GetMapping("/trial-details")
    fun getTrialDetails(): List<Map<String, Any>> {
        return reportService.fetchTrialDetails()
    }

    @GetMapping("/top-clients")
    fun getTopClients(
            @RequestParam(defaultValue = "10"
        ) limit: Int): ResponseEntity<TopClientResponse>
    {
        val topClients = reportService.getTopClients(limit)
        return ResponseEntity.ok(
          TopClientResponse(
              topClients.map {
                  it -> TopClient(it.first, it.second)
              }
          )
        )
    }

    @GetMapping("/unsigned-documents")
    fun getUnsignedDocuments(): List<Pair<String, Int>> {
        return reportService.findUnsignedDocuments()
    }

    @GetMapping("/case-decisions")
    fun getCaseDecisions(@RequestParam(defaultValue = "2000-01-01") fromDate: String): List<Map<String, Any>> {
        return reportService.listCaseDecisions(LocalDate.parse(fromDate))
    }

    @GetMapping("/monthly-payments/{clientId}")
    fun calculateMonthlyPayment(@PathVariable clientId: Int): ResponseEntity<Map<String, Any>> {
        val paymentValue = reportService.calculateMonthlyPayment(clientId)
        return ResponseEntity.ok(
            mapOf(
                "clientId" to clientId,
                "monthly_payment" to paymentValue
            )
        )
    }

    @GetMapping("/monthly-payments/all")
    fun calculateAllMonthlyPayments(): ResponseEntity<List<ClientPayment>> {
        val payments = reportService.calculateMonthlyPaymentForAllClients()
        return ResponseEntity.ok(payments)
    }
}