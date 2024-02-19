package com.logikcull.assignment.dto

import com.logikcull.assignment.model.LoadFileEntry
import com.logikcull.assignment.model.dto.ResponseDTO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ResponseDTOTest {

    @Test
    fun `test default values`() {
        val responseDTO = ResponseDTO()
        assertEquals("", responseDTO.description)
        assertEquals(emptyList<LoadFileEntry>(), responseDTO.data)
    }

    @Test
    fun `test custom values`() {
        val entry1 = LoadFileEntry("entry1", "", "")
        val entry2 = LoadFileEntry("entry2", "", "")
        val description = "Test Description"
        val data = listOf(entry1, entry2)

        val responseDTO = ResponseDTO(description, data)
        assertEquals(description, responseDTO.description)
        assertEquals(data, responseDTO.data)
    }

}