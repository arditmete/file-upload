package com.logikcull.assignment.service

import com.logikcull.assignment.model.LoadFileEntry
import com.logikcull.assignment.service.impl.ImportFileServiceImpl
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.mock.web.MockMultipartFile
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipException
import java.util.zip.ZipInputStream
import javax.validation.ValidationException

@ExtendWith(MockitoExtension::class)
class YourServiceTest {

    @InjectMocks
    lateinit var impl: ImportFileServiceImpl

    @Test
    fun `handleZipImport should return appropriate response for validation error`() {
        val mockedException = ValidationException("Invalid data")
        val multipartFile = mock(MockMultipartFile::class.java)
        `when`(multipartFile.inputStream).thenThrow(mockedException)

        val responseDTO = runBlocking { impl.handleZipImport(multipartFile) }

        assertEquals("Validation error: ${mockedException.message}", responseDTO.description)
    }

    @Test
    fun `handleZipImport should return appropriate response for unexpected error`() {
        val mockedException = IOException("Unexpected error")
        val multipartFile = mock(MockMultipartFile::class.java)
        `when`(multipartFile.inputStream).thenThrow(mockedException)

        val responseDTO = runBlocking { impl.handleZipImport(multipartFile) }

        assertEquals("An unexpected error occurred while importing ZIP file.", responseDTO.description)
    }

    private fun createTempFile(): File {
        val tempFile = File.createTempFile("temp", ".zip")
        tempFile.deleteOnExit()
        return tempFile
    }

    @Test
    suspend fun `handleZipImport should throw exception for corrupted zip file`() {
        val corruptedZipFile = File("src/test/resources/corrupted.zip")
        val multipartFile = MockMultipartFile(
            corruptedZipFile.name,
            corruptedZipFile.name,
            "application/zip",
            corruptedZipFile.inputStream()
        )

        assertThrows<ZipException> {
            impl.handleZipImport(multipartFile)
        }
    }

    @Test
    suspend fun `handleZipImport should throw exception for non-existing file`() {
        val nonExistingFile = File("non_existing_file.zip")
        val multipartFile = MockMultipartFile(
            nonExistingFile.name,
            nonExistingFile.name,
            "application/zip",
            nonExistingFile.inputStream()
        )

        assertThrows<IOException> {
            impl.handleZipImport(multipartFile)
        }
    }

    @Test
    suspend fun `handleZipImport should return appropriate response for zip file with no valid entries`() {
        val emptyEntriesZipFile = File("src/test/resources/empty_entries.zip")
        val multipartFile = MockMultipartFile(
            emptyEntriesZipFile.name,
            emptyEntriesZipFile.name,
            "application/zip",
            emptyEntriesZipFile.inputStream()
        )

        val responseDTO = impl.handleZipImport(multipartFile)

        assertEquals("There is no data!", responseDTO.description)
        assertTrue(responseDTO.data.isEmpty())
    }


    @Test
    suspend fun `processZipEntries should return ResponseDTO with no data when zip has no matching entries`() {
        val zipInputStream = ZipInputStream(ByteArrayInputStream(byteArrayOf()))
        val zipFile = MockMultipartFile("test.zip", byteArrayOf())

        val responseDTO = impl.processZipEntries(zipInputStream, zipFile)

        assertEquals("There is no data!", responseDTO.description)
        assertEquals(emptyList<LoadFileEntry>(), responseDTO.data)
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

    @Test
    fun `test on matching entry`() {
        val entry = ZipEntry("example.txt")
        val fileName = "example.zip"
        assert(impl.matchesFileName(entry, fileName))
    }

}