package international.tourism.app.repo

import com.google.gson.Gson
import international.tourism.app.models.Booking
import international.tourism.app.utils.ApiRequest
import international.tourism.app.utils.ApiResponse

class BookingService
{
    fun bookingAllData(booking: Booking): ApiResponse
    {
        return ApiRequest.post(ApiRequest.BOOKING_URL, Gson().toJson(booking))
    }

    fun bookingHotel(booking: Booking): ApiResponse
    {
        return ApiRequest.post(ApiRequest.BOOKING_URL, Gson().toJson(booking))
    }
}