package model.Logikcull.assignment.service

import model.Logikcull.assignment.model.dto.ResponseDTO
import model.Logikcull.assignment.model.enums.FileType
import org.springframework.web.multipart.MultipartFile

interface UploadFileService {
    fun getFileType(fileName: String): FileType
    fun handleFileUpload(file: MultipartFile): ResponseDTO
}
