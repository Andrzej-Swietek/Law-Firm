package pl.swietek.law_firm.exceptions

class CourtNotFoundException(override val message: String) : RuntimeException(message) {

}