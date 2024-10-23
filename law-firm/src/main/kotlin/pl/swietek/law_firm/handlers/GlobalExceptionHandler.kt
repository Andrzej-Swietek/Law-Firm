package pl.swietek.law_firm.handlers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
//import org.springframework.web.ErrorResponse
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import pl.swietek.law_firm.exceptions.ClientNotFoundException
import java.util.function.Consumer

import pl.swietek.law_firm.handlers.ErrorResponse;

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ClientNotFoundException::class)
    fun handleCustomerNotFoundException(
        e: ClientNotFoundException
    ): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(e.message)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleCustomerNotFoundException(
        e: MethodArgumentNotValidException
    ): ResponseEntity<ErrorResponse> {
        val errors = HashMap<String, String?>()
        e.bindingResult
            .fieldErrors
            .forEach(Consumer { error: FieldError ->
                errors[error.field] = error.defaultMessage
            })
        val response = ErrorResponse(errors)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }
}