package com.logikcull.assignment.service

import com.logikcull.assignment.model.dto.ResponseDTO
import com.logikcull.assignment.model.enums.FileType
import org.springframework.web.multipart.MultipartFile

interface UploadFileService {
    fun getFileType(fileName: String): FileType
    fun handleFileUpload(file: MultipartFile): ResponseDTO
}
