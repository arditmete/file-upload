package com.logikcull.assignment.exception

import com.logikcull.assignment.model.dto.ErrorResponseDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler(CustomException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleCustomException(e: CustomException): ErrorResponseDto {
        return ErrorResponseDto(errorCode = e.code.getCode(), message = e.code.getMsg(), details = e.details)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleRequestBodyNotValidException(exception: MethodArgumentNotValidException?): ErrorResponseDto {
        return ErrorResponseDto(ErrorCode.INCORRECT_REQUEST_BODY.getCode(), ErrorCode.INCORRECT_REQUEST_BODY.getMsg())
    }
}