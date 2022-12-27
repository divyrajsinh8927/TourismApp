package international.tourism.app.repo

import international.tourism.app.utils.ApiRequest
import international.tourism.app.utils.ApiResponse
import com.google.gson.Gson
import international.tourism.app.models.*

class AuthService
{
    fun login(user: User): ApiResponse
    {
        val user = Gson().toJson(user)
        return ApiRequest.post(ApiRequest.LOGIN_URL, user)
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

    fun country(country: Country): ApiResponse
    {
        return ApiRequest.get(ApiRequest.COUNTRY_URL)
    }

    fun place(place: Place): ApiResponse
    {
        return ApiRequest.get(ApiRequest.PLACE_URL)
    }

    fun hotel(hotel: Hotel): ApiResponse
    {
        return ApiRequest.get(ApiRequest.HOTEL_URL)
    }

    fun bookingData(booking: Booking): ApiResponse
    {
        return ApiRequest.get(ApiRequest.BOOKING_URL)
    }

    fun bookingHotel(booking: Booking): ApiResponse
    {
        return ApiRequest.post(ApiRequest.BOOKING_URL, Gson().toJson(booking))
    }

}