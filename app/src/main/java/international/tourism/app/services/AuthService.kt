package international.tourism.app.services

import international.tourism.app.utils.ApiRequest
import international.tourism.app.utils.ApiResponse
import com.google.gson.Gson
import international.tourism.app.models.*

class AuthService
{
    fun login(user: User): ApiResponse
    {
        return ApiRequest.post(ApiRequest.LOGIN_URL,Gson().toJson(user))
    }

    fun register(user: User): ApiResponse
    {
        return ApiRequest.post(ApiRequest.REGISTER_URL, Gson().toJson(user))
    }

    fun getUser(user: User): ApiResponse
    {
        return ApiRequest.post(ApiRequest.REGISTER_URL,Gson().toJson(user))
    }

    fun changePassword(user: User): ApiResponse
    {
        return ApiRequest.put(ApiRequest.REGISTER_URL,Gson().toJson(user))
    }

    fun addToken(user: User): ApiResponse
    {
        return ApiRequest.get(ApiRequest.ADDTOKEN_URL.plus("?userId=").plus(user.Id).plus("&token=".plus(user.FirebaseToken)))
    }

    fun verifyEmailAndSendOtp(user: User): ApiResponse
    {
        return ApiRequest.post(ApiRequest.SENDOTP_URL,Gson().toJson(user))
    }

    fun resetPassword(forgotPasswordOtp: ForgotPasswordOtp): ApiResponse
    {
        return ApiRequest.post(ApiRequest.RESETPASSWORD_URL,Gson().toJson(forgotPasswordOtp))
    }

}