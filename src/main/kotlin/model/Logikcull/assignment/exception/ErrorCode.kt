package model.Logikcull.assignment.exception

import lombok.Data
import lombok.RequiredArgsConstructor

@Data
@RequiredArgsConstructor
enum class ErrorCode(private val code: Int, private val msg: String) {
    INCORRECT_REQUEST_BODY(1000, Constants.INCORRECT_REQUEST_BODY_MSG),
    INTERNAL_ERROR_SERVER_HAS_NO_DATA(1004, Constants.INTERNAL_ERROR_SERVER_HAS_NO_DATA_MSG),
    NO_SUCH_CURRENCY(1005, Constants.NO_SUCH_CURRENCY_MSG);

    object Constants {
        const val INCORRECT_REQUEST_BODY_MSG = "Please check again your inputs. Base, symbol and amount must have three characters and should not be empty."
        const val INTERNAL_ERROR_SERVER_HAS_NO_DATA_MSG = "Problem with the server. Please contact an administrator"
        const val NO_SUCH_CURRENCY_MSG = "Currency code is invalid"
    }
    fun getMsg(): String {
        return msg
    }
    fun getCode(): Int{
        return code
    }

}