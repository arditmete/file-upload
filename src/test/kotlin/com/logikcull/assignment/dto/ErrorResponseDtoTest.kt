package com.logikcull.assignment.dto

import com.logikcull.assignment.model.dto.ErrorResponseDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ErrorResponseDtoTest {

    @Test
    fun testErrorResponseDto() {
        val errorCode = 404
        val message = "Resource not found"
        val details = listOf("Detail 1", "Detail 2")

        val errorResponseDto = ErrorResponseDto(errorCode, message, details)

        assertEquals(errorCode, errorResponseDto.errorCode)
        assertEquals(message, errorResponseDto.message)
        assertEquals(details, errorResponseDto.details)
    }
}