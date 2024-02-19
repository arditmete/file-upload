package com.logikcull.assignment.service.impl

import com.logikcull.assignment.model.LoadFileEntry
import com.logikcull.assignment.model.dto.ResponseDTO
import com.logikcull.assignment.model.enums.FileType
import com.logikcull.assignment.parser.LoadFileParser
import com.logikcull.assignment.service.ImportFileService
import com.logikcull.assignment.validator.Validator
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.validation.ValidationException

@Service
class ImportFileServiceImpl : ImportFileService {
    private val NUM_THREADS = minOf(4, Runtime.getRuntime().availableProcessors() * 2)

    override suspend fun handleZipImport(zipFile: MultipartFile): ResponseDTO {
        return try {
            val entries = processZipFile(zipFile)
                ResponseDTO(description = "File imported successfully.", data = entries.data)
        } catch (e: ValidationException) {
            ResponseDTO(description = "Validation error: ${e.message}")
        } catch (e: Exception) {
            // Log the exception for debugging purposes
            println("An unexpected error occurred while importing ZIP file: ${e.message}")
            ResponseDTO(description = "An unexpected error occurred while importing ZIP file.")
        }
    }

    private suspend fun processZipFile(zipFile: MultipartFile): ResponseDTO {

        val deferredEntries = mutableListOf<Deferred<List<LoadFileEntry>>>()

        val inputStream = zipFile.inputStream
        val zipInputStream = ZipInputStream(inputStream)

//        repeat(NUM_THREADS) {
//            val deferred = CoroutineScope(Dispatchers.IO).async { // Specify the dispatcher
//                processZipEntries(zipInputStream, zipFile.originalFilename.toString().removeSuffix(".zip"))
//            }
//            deferredEntries.add(deferred)
//        }
//
//        deferredEntries.forEach {
//            entries.addAll(it.await())
//        }
        val deferred = CoroutineScope(Dispatchers.IO).async { // Specify the dispatcher
            zipp(zipInputStream, zipFile)
        }
        return deferred.await()
    }

    private suspend fun zipp(zipInputStream: ZipInputStream, zipFile: MultipartFile): ResponseDTO {
        val entries = mutableListOf<LoadFileEntry>()
        var entry = zipInputStream.nextEntry
        while (entry != null) {
            if (!entry.isDirectory && entry.name.startsWith(zipFile.originalFilename.toString().removeSuffix(".zip"))) {
                val reader = InputStreamReader(zipInputStream.readBytes().inputStream())
                when {
                    isLfpEntry(entry) -> entries.addAll(LoadFileParser.parseCsv(reader))
                    isXlfEntry(entry) -> entries.addAll(LoadFileParser.parseXml(reader))
                }
            }
            entry = zipInputStream.nextEntry
        }
        Validator.validateControlNumbers(entries)
        Validator.validateImagePathExtensions(entries)
        return ResponseDTO(
                description = if(entries.isEmpty()) "There is no data!" else "File imported successfully.",
                data = entries
        )
    }

    private suspend fun processZipEntries(zipInputStream: ZipInputStream, zipFileName: String): List<LoadFileEntry> {
        val parsedEntries = mutableListOf<LoadFileEntry>()
        var entry: ZipEntry?
        try {
            while (zipInputStream.nextEntry.also { entry = it } != null) {
                if (!entry!!.isDirectory && shouldProcessEntry(entry!!, zipFileName)) {
                    val reader = InputStreamReader(zipInputStream)
                    try {
                        parsedEntries.addAll(processZipEntry(reader, entry!!))
                    } catch (e: Exception) {
                        println("An error occurred while processing entry ${entry!!.name}: ${e.message}")
                        // Continue processing other entries
                    }
                }
            }
        } catch (e: IOException) {
            println("An error occurred while reading the ZIP file: ${e.message}")
            // Handle the IOException
        }
        return parsedEntries
    }

    private suspend fun processZipEntry(reader: InputStreamReader, entry: ZipEntry): List<LoadFileEntry> {
        val parsedEntries = when {
            isLfpEntry(entry) -> LoadFileParser.parseCsv(reader)
            isXlfEntry(entry) -> LoadFileParser.parseXml(reader)
            else -> emptyList()
        }
        println("Processing entry: ${entry.name}")
        println("Number of entries parsed: ${parsedEntries.size}")
        return parsedEntries
    }

    private fun shouldProcessEntry(entry: ZipEntry, zipFileName: String): Boolean {
        return entry.name.startsWith(zipFileName, ignoreCase = true)
    }

    private fun isLfpEntry(entry: ZipEntry): Boolean {
        return entry.name.lowercase(Locale.getDefault()).endsWith(FileType.LFP.name.lowercase(Locale.getDefault()))
    }

    private fun isXlfEntry(entry: ZipEntry): Boolean {
        return entry.name.lowercase(Locale.getDefault()).endsWith(FileType.XLF.name.lowercase(Locale.getDefault()))
    }
}
