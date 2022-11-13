package com.spark.issueservice.exception

import com.auth0.jwt.exceptions.TokenExpiredException
import mu.KotlinLogging
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = KotlinLogging.logger {}

    @ExceptionHandler(ServerException::class)
    fun handleServerExceptionHandler(ex : ServerException) : ErrorResponse {
        log.error { ex.message }

        return ErrorResponse(code = ex.code, message = ex.message)
    }

    @ExceptionHandler(TokenExpiredException::class)
    fun handleTokenExpiredException(ex : TokenExpiredException) : ErrorResponse {
        log.error { ex.message }

        return ErrorResponse(code = 401, message = "Token Expired Error")
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex : Exception) : ErrorResponse {
        log.error { ex.message }

        return ErrorResponse(code = 500, message = "Internal Server Error")
    }
}