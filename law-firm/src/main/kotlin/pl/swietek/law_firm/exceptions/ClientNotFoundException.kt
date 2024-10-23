package pl.swietek.law_firm.exceptions

class ClientNotFoundException(override val message: String) : RuntimeException(message) {

}