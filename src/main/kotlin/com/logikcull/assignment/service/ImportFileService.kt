package com.logikcull.assignment.service

import com.logikcull.assignment.model.dto.ResponseDTO
import org.springframework.web.multipart.MultipartFile

interface ImportFileService {
    suspend fun handleZipImport(zipFile: MultipartFile): ResponseDTO
}
