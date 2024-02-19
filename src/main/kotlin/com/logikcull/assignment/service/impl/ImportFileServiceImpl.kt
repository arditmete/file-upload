package com.logikcull.assignment.service.impl

import com.logikcull.assignment.model.LoadFileEntry
import com.logikcull.assignment.model.dto.ResponseDTO
import com.logikcull.assignment.model.enums.FileType
import com.logikcull.assignment.parser.LoadFileParser
import com.logikcull.assignment.service.ImportFileService
import com.logikcull.assignment.validator.Validator
import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.validation.ValidationException

@Service
class ImportFileServiceImpl : ImportFileService {

    override suspend fun handleZipImport(zipFile: MultipartFile): ResponseDTO {
        return try {
            processZipFile(zipFile)
        } catch (e: ValidationException) {
            ResponseDTO(description = "Validation error: ${e.message}")
        } catch (e: Exception) {
            ResponseDTO(description = "An unexpected error occurred while importing ZIP file.")
        }
    }

    private suspend fun processZipFile(zipFile: MultipartFile): ResponseDTO {
        val inputStream = zipFile.inputStream
        val zipInputStream = ZipInputStream(inputStream)
        val deferred = CoroutineScope(Dispatchers.IO).async {
            processZipEntries(zipInputStream, zipFile)
        }
        return deferred.await()
    }

    suspend fun processZipEntries(zipInputStream: ZipInputStream, zipFile: MultipartFile): ResponseDTO {
        val entries = mutableListOf<LoadFileEntry>()
        var entry = zipInputStream.nextEntry
        while (entry != null) {
            if (!entry.isDirectory && matchesFileName(entry, zipFile.originalFilename.toString())) {
                val reader = InputStreamReader(zipInputStream.readBytes().inputStream())
                when {
                    isLfpEntry(entry) -> entries.addAll(LoadFileParser.parseCsv(reader))
                    isXlfEntry(entry) -> entries.addAll(LoadFileParser.parseXml(reader))
                }
            }
            entry = zipInputStream.nextEntry
        }
        Validator.validate(entries)
        return ResponseDTO(
                description = if(entries.isEmpty()) "There is no data!" else "File imported successfully.",
                data = entries
        )
    }

    fun matchesFileName(entry: ZipEntry, fileName: String): Boolean {
        val fileNameWithoutExtension = fileName.removeSuffix(".zip")
        return !entry.isDirectory && entry.name.startsWith(fileNameWithoutExtension)
    }

    fun isLfpEntry(entry: ZipEntry): Boolean {
        return entry.name.lowercase(Locale.getDefault()).endsWith(FileType.LFP.name.lowercase(Locale.getDefault()))
    }

    fun isXlfEntry(entry: ZipEntry): Boolean {
        return entry.name.lowercase(Locale.getDefault()).endsWith(FileType.XLF.name.lowercase(Locale.getDefault()))
    }
}
