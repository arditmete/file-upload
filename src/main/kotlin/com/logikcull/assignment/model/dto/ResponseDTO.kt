package com.logikcull.assignment.model.dto

import com.logikcull.assignment.model.LoadFileEntry

data class ResponseDTO(
    var description: String = "",
    var data: List<LoadFileEntry> = mutableListOf()
)