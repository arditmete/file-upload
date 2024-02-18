package com.logikcull.assignment.controller

import com.logikcull.assignment.model.dto.ResponseDTO
import com.logikcull.assignment.service.ImportFileService
import com.logikcull.assignment.validator.Validator
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
class LoadFileController(val importFileService: ImportFileService) {
    @PostMapping("/process")
    fun import(@RequestParam("file") file: MultipartFile): ResponseDTO {
        Validator.isZipFile(file)
        return importFileService.handleZipImport(file)
    }
}