package com.logikcull.assignment.validator

import com.logikcull.assignment.exception.CustomException
import com.logikcull.assignment.exception.ErrorCode
import com.logikcull.assignment.model.LoadFileEntry
import org.springframework.web.multipart.MultipartFile
import java.util.*

class Validator {
    companion object {
        private val regex = Regex("^[a-zA-Z]+-\\d{6}$")
        private val allowedExtensions = setOf("tif", "jpg", "png", "pdf")

        fun validateControlNumbers(entries: List<LoadFileEntry>) {
            val errorList = entries.filterNot { entry -> entry.controlNumber.matches(regex) }
                .map { entry -> "ControlNumber ${entry.controlNumber} does not match!" }
            if (errorList.isNotEmpty()) {
                throw CustomException(ErrorCode.CONTROL_NUMBER_NOT_MATCH, details = errorList)
            }
        }

        fun isZipFile(zipFile: MultipartFile) {
            if (!zipFile.originalFilename.toString().endsWith(".zip")) {
                throw CustomException(ErrorCode.NON_ZIP_FILE)
            }
        }

        fun validateImagePathExtensions(entries: List<LoadFileEntry>) {
            val invalidEntries = entries.filter { entry ->
                val extension = entry.path.substringAfterLast(".").lowercase(Locale.getDefault())
                extension !in allowedExtensions
            }
            if (invalidEntries.isNotEmpty()) {
                val errorMessages = invalidEntries.map { entry ->
                    "Invalid file extension for path ${entry.path}."
                }
                throw CustomException(ErrorCode.INVALID_FILE_EXTENSION, details = errorMessages)
            }
        }
    }
}