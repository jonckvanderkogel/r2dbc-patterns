package com.bullet.r2dbcpatterns.controller

import com.bullet.r2dbcpatterns.dto.Invalid
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerAdvice {
    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentException(ex: IllegalArgumentException): ResponseEntity<Invalid> = ResponseEntity(Invalid(ex.message ?: "Error handling request"), BAD_REQUEST)

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun dataIntegrityException(ex: DataIntegrityViolationException): ResponseEntity<Invalid> = ResponseEntity(Invalid("Violated data integrity with that request"), BAD_REQUEST)
}