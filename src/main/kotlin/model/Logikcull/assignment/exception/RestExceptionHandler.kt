package model.Logikcull.assignment.exception

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
        return ErrorResponseDto(e.code.getCode(), e.details)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleRequestBodyNotValidException(exception: MethodArgumentNotValidException?): ErrorResponseDto {
        return ErrorResponseDto(ErrorCode.INCORRECT_REQUEST_BODY.getCode())
    }
}