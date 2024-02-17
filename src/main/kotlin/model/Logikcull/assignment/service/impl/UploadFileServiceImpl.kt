package model.Logikcull.assignment.service.impl

import com.Logikcull.assignment.parser.LoadFileParser
import com.Logikcull.assignment.validator.LoadFileValidator
import model.Logikcull.assignment.model.dto.ResponseDTO
import model.Logikcull.assignment.model.enums.FileStatus
import model.Logikcull.assignment.model.enums.FileType
import model.Logikcull.assignment.service.UploadFileService
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
        LoadFileValidator.validateControlNumbers(result)
        LoadFileValidator.validateImagePathExtensions(result)
        return ResponseDTO(
                status = FileStatus.SUCCESS,
                description = "File uploaded successfully.",
                data = result)
    }

    }
