package model.Logikcull.assignment.model.dto

import com.Logikcull.assignment.model.LoadFileEntry
import model.Logikcull.assignment.model.enums.FileStatus

data class ResponseDTO(
        var status: FileStatus,
        var description: String = "",
        var data: List<LoadFileEntry> = mutableListOf()
)