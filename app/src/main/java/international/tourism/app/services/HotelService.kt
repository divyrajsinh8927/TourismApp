package international.tourism.app.services

import com.google.gson.Gson
import international.tourism.app.models.HotelImage
import international.tourism.app.utils.ApiRequest
import international.tourism.app.utils.ApiResponse

class HotelService
{
    fun getAllHotel(): ApiResponse
    {
        return ApiRequest.get(ApiRequest.HOTEL_URL)
    }

    fun getHotelImages(hotelImage: HotelImage): ApiResponse
    {
        return ApiRequest.post(ApiRequest.HOTEL_URL, Gson().toJson(hotelImage))
    }
}