package com.logikcull.assignment.model.dto

import com.logikcull.assignment.model.LoadFileEntry
import com.logikcull.assignment.model.enums.FileStatus

data class ResponseDTO(
    var status: FileStatus,
    var description: String = "",
    var data: List<LoadFileEntry> = mutableListOf()
)