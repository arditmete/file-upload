package com.logikcull.assignment.service.impl

import com.logikcull.assignment.model.LoadFileEntry
import com.logikcull.assignment.model.dto.ResponseDTO
import com.logikcull.assignment.model.enums.FileType
import com.logikcull.assignment.parser.LoadFileParser
import com.logikcull.assignment.service.ImportFileService
import com.logikcull.assignment.validator.Validator
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.InputStreamReader
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

@Service
class ImportFileServiceImpl : ImportFileService {
    override fun handleZipImport(zipFile: MultipartFile): ResponseDTO {
        val entries = mutableListOf<LoadFileEntry>()
        val zipInputStream = ZipInputStream(zipFile.inputStream)
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

    fun isLfpEntry(entry: ZipEntry): Boolean {
        return entry.name.lowercase(Locale.getDefault()).endsWith(FileType.LFP.name.lowercase(Locale.getDefault()))
    }

    fun isXlfEntry(entry: ZipEntry): Boolean {
        return entry.name.lowercase(Locale.getDefault()).endsWith(FileType.XLF.name.lowercase(Locale.getDefault()))
    }
}
