package com.logikcull.assignment.exception

data class ErrorResponseDto(
        var errorCode: Int,
        var details: String? = null
    )