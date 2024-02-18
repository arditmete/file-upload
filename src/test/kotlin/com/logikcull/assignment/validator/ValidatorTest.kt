package com.logikcull.assignment.validator

import com.logikcull.assignment.exception.CustomException
import com.logikcull.assignment.exception.ErrorCode
import com.logikcull.assignment.model.LoadFileEntry
import com.logikcull.assignment.validator.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.mock.web.MockMultipartFile

class ValidatorTest {
    @Test
    @DisplayName("Test validateControlNumbers with valid control numbers")
    fun testValidateControlNumbersValid() {
        val entries = listOf(
            LoadFileEntry("ABC-123456", "Volume1", "Path1/Image1"),
            LoadFileEntry("DEF-654321", "Volume2", "Path2/Image2")
        )

        Validator.validateControlNumbers(entries)
    }

    @Test
    @DisplayName("Test validateControlNumbers with invalid control numbers")
    fun testValidateControlNumbersWithInvalidControlNumbers() {
        val entries = listOf(
            LoadFileEntry("InvalidControlNumber", "Volume1", "Path1/Image1"),
            LoadFileEntry("DEF-654321", "Volume2", "Path2/Image2")
        )
        val exception = assertThrows<CustomException> {
            Validator.validateControlNumbers(entries)
        }
        assertEquals(ErrorCode.CONTROL_NUMBER_NOT_MATCH, exception.code)
    }

    @Test
    @DisplayName("Test isZipFile with valid zip file")
    fun testisZipFile() {
        val zipFile = MockMultipartFile("file", "test.zip", "application/zip", ByteArray(0))

        Validator.isZipFile(zipFile)
    }

    @Test
    @DisplayName("Test isZipFile with invalid zip file")
    fun isZipFileTest(){
        val zipFile = MockMultipartFile("file", "test.txt", "text/plain", ByteArray(0))

        val exception = assertThrows<CustomException> {
            Validator.isZipFile(zipFile)
        }
        assertEquals(ErrorCode.NON_ZIP_FILE, exception.code)
    }

    @Test
    @DisplayName("Test validateImagePathExtensions with valid extensions")
    fun validateControlNumbersTest() {
        val entries = listOf(
            LoadFileEntry("ABC-123456", "Volume1", "Path1/Image1.jpg"),
            LoadFileEntry("DEF-654321", "Volume2", "Path2/Image2.png")
        )

        Validator.validateImagePathExtensions(entries)
    }

    @Test
    @DisplayName("Test validateImagePathExtensions with invalid extensions")
    fun testValidateImagePathExtensionsWithInvalidExtensions() {
        val entries = listOf(
            LoadFileEntry("ABC-123456", "Volume1", "Path1/Image1.txt"),
            LoadFileEntry("DEF-654321", "Volume2", "Path2/Image2.pdf")
        )
        val exception = assertThrows<CustomException> {
            Validator.validateImagePathExtensions(entries)
        }
        assertEquals(ErrorCode.INVALID_FILE_EXTENSION, exception.code)
    }
}