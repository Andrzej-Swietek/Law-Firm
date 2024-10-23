package pl.swietek.law_firm.handlers

data class ErrorResponse(
  val errors: Map<String, String?>
)
