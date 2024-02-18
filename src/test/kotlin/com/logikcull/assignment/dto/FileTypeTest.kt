package com.logikcull.assignment.dto

import com.logikcull.assignment.model.enums.FileType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FileTypeTest {

    @Test
    fun testFileType_XLF() {
        val fileType = FileType.XLF
        assertEquals("XLF", fileType.name)
    }

    @Test
    fun testFileType_LFP() {
        val fileType = FileType.LFP
        assertEquals("LFP", fileType.name)
    }

    @Test
    fun testFileType_Values() {
        val values = FileType.values()
        assertEquals(2, values.size)
        assertEquals(FileType.XLF, values[0])
        assertEquals(FileType.LFP, values[1])
    }

    @Test
    fun testFileType_ValueOf() {
        val fileType = FileType.valueOf("XLF")
        assertEquals(FileType.XLF, fileType)
    }
}