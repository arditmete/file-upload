package model.Logikcull.assignment.exception

import lombok.Getter

@Getter
data class CustomException(val code: ErrorCode, val details: String) : RuntimeException(details)
