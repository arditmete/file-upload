package com.logikcull.assignment.model.dto

data class ErrorResponseDto(
        var errorCode: Int,
        var message: String,
        var details: List<String>? = null
    )