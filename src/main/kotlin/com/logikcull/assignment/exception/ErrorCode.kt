package com.logikcull.assignment.exception

import lombok.Data
import lombok.RequiredArgsConstructor

@Data
@RequiredArgsConstructor
enum class ErrorCode(private val code: Int, private val msg: String) {
    INCORRECT_REQUEST_BODY(5000, Constants.INCORRECT_REQUEST_BODY_MSG),
    CONTROL_NUMBER_NOT_MATCH(5001, Constants.CONTROL_NUMBER_NOT_MATCH),
    INVALID_FILE_EXTENSION(5002, Constants.INVALID_FILE_EXTENSION),
    NON_ZIP_FILE(5003, Constants.NON_ZIP_FILE);

    object Constants {
        const val INCORRECT_REQUEST_BODY_MSG = "Please check again your inputs."
        const val CONTROL_NUMBER_NOT_MATCH = "Control number doesn't match!"
        const val INVALID_FILE_EXTENSION =  "Invalid file extension!"
        const val NON_ZIP_FILE =  "File format not supported!"
    }
    fun getMsg(): String {
        return msg
    }
    fun getCode(): Int{
        return code
    }

}