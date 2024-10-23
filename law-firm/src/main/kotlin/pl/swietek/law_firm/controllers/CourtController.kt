package pl.swietek.law_firm.controllers

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.swietek.law_firm.services.CourtService

@RestController
@RequestMapping("/api/v1/court")
class CourtController(private val courtService: CourtService) {

}