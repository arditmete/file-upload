package com.logikcull.assignment.service.impl

import com.logikcull.assignment.model.dto.ResponseDTO
import com.logikcull.assignment.model.enums.FileStatus
import com.logikcull.assignment.model.enums.FileType
import com.logikcull.assignment.parser.LoadFileParser
import com.logikcull.assignment.service.UploadFileService
import com.logikcull.assignment.validator.Validator
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

@Service
class UploadFileServiceImpl: UploadFileService {
    override fun getFileType(fileName: String): FileType {
        return when (fileName.substringAfterLast(".").lowercase(Locale.getDefault())) {
            FileType.LFP.name.lowercase() -> FileType.LFP
            else -> FileType.XLF
        }
    }

    override fun handleFileUpload(file: MultipartFile): ResponseDTO {

        val fileName = StringUtils.cleanPath(file.originalFilename!!)
        val filePath = "uploads/$fileName"
        val targetFile = File(filePath)
        try {
            BufferedOutputStream(FileOutputStream(targetFile)).use { outputStream ->
                outputStream.write(file.bytes)
            }
        } catch (ex: Exception) {
            return ResponseDTO(
                    status = FileStatus.ERROR,
                    description = "File failed to upload! stacktrace-> ${ex.message}",
                    data = listOf())
        }
        val result = when (getFileType(fileName)) {
            FileType.LFP-> LoadFileParser.parseCsv(targetFile)
            FileType.XLF -> LoadFileParser.parseXml(targetFile)
        }
        Validator.validateControlNumbers(result)
        Validator.validateImagePathExtensions(result)
        return ResponseDTO(
                status = FileStatus.SUCCESS,
                description = "File uploaded successfully.",
                data = result)
    }

    }
