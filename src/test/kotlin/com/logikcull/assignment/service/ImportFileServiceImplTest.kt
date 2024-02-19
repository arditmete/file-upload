package com.logikcull.assignment.service

import com.logikcull.assignment.service.impl.ImportFileServiceImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.mock.web.MockMultipartFile
import java.io.File
import java.util.*
import java.util.zip.ZipEntry

@ExtendWith(MockitoExtension::class)
class YourServiceTest {

    @InjectMocks
    lateinit var impl: ImportFileServiceImpl

    @Test
    suspend fun `test handleZipImport`() {
        val file = File("src/test/resources/data.zip")


        val multipartFile = MockMultipartFile(
            file.name,
            file.name,
            "application/zip",
            file.inputStream()
        )

        val responseDTO = impl.handleZipImport(multipartFile)
        assertEquals("File imported successfully.", responseDTO.description)
        assertEquals(6, responseDTO.data.size)
    }
    @Test
    fun testIsLfpEntry_withLfpExtension_returnsTrue() {
        val zipEntry = ZipEntry("example.lfp")
        assertTrue(impl.isLfpEntry(zipEntry))
    }

    @Test
    fun testIsLfpEntry_withUpperCaseLfpExtension_returnsTrue() {
        val zipEntry = ZipEntry("example.LFP")
        assertTrue(impl.isLfpEntry(zipEntry))
    }

    @Test
    fun testIsLfpEntry_withNonLfpExtension_returnsFalse() {
        val zipEntry = ZipEntry("example.txt")
        assertFalse(impl.isLfpEntry(zipEntry))
    }

    @Test
    fun testIsXlfEntry_withXlfExtension_returnsTrue() {
        val zipEntry = ZipEntry("example.xlf")
        assertTrue(impl.isXlfEntry(zipEntry))
    }

    @Test
    fun testIsXlfEntry_withUpperCaseXlfExtension_returnsTrue() {
        val zipEntry = ZipEntry("example.XLF")
        assertTrue(impl.isXlfEntry(zipEntry))
    }

    @Test
    fun testIsXlfEntry_withNonXlfExtension_returnsFalse() {
        val zipEntry = ZipEntry("example.txt")
        assertFalse(impl.isXlfEntry(zipEntry))
    }
}