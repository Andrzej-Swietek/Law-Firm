package pl.swietek.law_firm.exceptions

class DocumentNotFoundException(override val message: String) : RuntimeException(message) {

}