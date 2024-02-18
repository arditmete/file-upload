package com.logikcull.assignment.dto

import com.logikcull.assignment.exception.ErrorCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ErrorCodeTest {

    @Test
    fun testErrorCodeValues() {
        val incorrectRequestBody = ErrorCode.INCORRECT_REQUEST_BODY
        assertEquals(5000, incorrectRequestBody.getCode())
        assertEquals("Please check again your inputs.", incorrectRequestBody.getMsg())

        val controlNumberNotMatch = ErrorCode.CONTROL_NUMBER_NOT_MATCH
        assertEquals(5001, controlNumberNotMatch.getCode())
        assertEquals("Control number doesn't match!", controlNumberNotMatch.getMsg())

        val invalidFileExtension = ErrorCode.INVALID_FILE_EXTENSION
        assertEquals(5002, invalidFileExtension.getCode())
        assertEquals("Invalid file extension!", invalidFileExtension.getMsg())

        val nonZipFile = ErrorCode.NON_ZIP_FILE
        assertEquals(5003, nonZipFile.getCode())
        assertEquals("File format not supported!", nonZipFile.getMsg())
    }
}