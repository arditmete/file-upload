package com.Logikcull.assignment.controller

import model.Logikcull.assignment.model.dto.ResponseDTO
import model.Logikcull.assignment.service.UploadFileService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
class LoadFileController(val uploadFileService: UploadFileService) {

    @PostMapping("/upload")
    fun upload(@RequestParam("file") file: MultipartFile): ResponseDTO {
        return uploadFileService.handleFileUpload(file)
    }
}