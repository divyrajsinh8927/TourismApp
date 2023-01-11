package international.tourism.app.repo

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
}