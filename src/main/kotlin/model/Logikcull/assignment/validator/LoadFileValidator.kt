package com.Logikcull.assignment.validator

import com.Logikcull.assignment.model.LoadFileEntry
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Component
import java.util.*

@Component
class LoadFileValidator {
    companion object {
        fun validateControlNumbers(entries: List<LoadFileEntry>): Boolean {
            val regex = Regex("^[a-zA-Z]+-\\d{6}$")
            return if(entries.all { entry -> entry.controlNumber.matches(regex) })
                true
            else
                throw BadRequestException()
        }

        fun validateImagePathExtensions(entries: List<LoadFileEntry>): Boolean {
            val allowedExtensions = setOf("tif", "jpg", "png", "pdf")
            return entries.all { entry ->
                val extension = entry.path.substringAfterLast(".").lowercase(Locale.getDefault())
                allowedExtensions.contains(extension)
            }
        }
    }
}