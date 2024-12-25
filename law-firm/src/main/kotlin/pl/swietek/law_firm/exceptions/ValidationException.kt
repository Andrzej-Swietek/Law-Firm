package pl.swietek.law_firm.exceptions

import lombok.AllArgsConstructor
import lombok.Data
import lombok.EqualsAndHashCode


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
class ValidationException(errors: List<String>) : RuntimeException() {
    private val errors: List<String>? = null
    override val message: String = errors.joinToString(separator = "\n")

    fun getErrors() = run { errors }
}