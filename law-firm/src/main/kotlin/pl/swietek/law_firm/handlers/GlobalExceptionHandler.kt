package pl.swietek.law_firm.handlers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.BadSqlGrammarException
import org.springframework.validation.FieldError
import org.springframework.web.HttpRequestMethodNotSupportedException
//import org.springframework.web.ErrorResponse
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import pl.swietek.law_firm.exceptions.ClientNotFoundException
import pl.swietek.law_firm.exceptions.CourtNotFoundException
import pl.swietek.law_firm.exceptions.DocumentNotFoundException
import pl.swietek.law_firm.exceptions.ValidationException
import java.util.function.Consumer

import pl.swietek.law_firm.handlers.ErrorResponse;
import sun.security.timestamp.TSResponse.BAD_REQUEST

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ClientNotFoundException::class)
    fun handleCustomerNotFoundException(
        e: ClientNotFoundException
    ): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(e.message)
    }

    @ExceptionHandler(CourtNotFoundException::class)
    fun handleCourtNotFoundException(
        e: CourtNotFoundException
    ): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(e.message)
    }

    @ExceptionHandler(DocumentNotFoundException::class)
    fun handleCourtNotFoundException(
        e: DocumentNotFoundException
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

    @ExceptionHandler(BadSqlGrammarException::class)
    fun handleBadSqlGrammarException(
        e: BadSqlGrammarException
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            errors = mapOf("SQL Error" to e.sqlException?.message, "sql" to e.sql),
        )
        //  "A database syntax error occurred. Please check the SQL query and schema configuration."
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(errorResponse)
    }

    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationFailedException( exception: ValidationException): ResponseEntity<ErrorResponse>  {
        val errorResponse = ErrorResponse(
            errors = mapOf(
                "message" to exception.message,
                "errors" to (exception.getErrors()?.joinToString(separator = "\n ") ?: "no errors"),
            ),
        )
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponse)
    }

//    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
//    fun handleMethodNotSupported(exception: HttpRequestMethodNotSupportedException): ResponseEntity<String> {
//        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
//            .body("Error: ${exception.message}")
//    }
}