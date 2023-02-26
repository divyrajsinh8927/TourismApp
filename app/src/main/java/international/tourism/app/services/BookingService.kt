package international.tourism.app.services

import com.google.gson.Gson
import international.tourism.app.models.Booking
import international.tourism.app.utils.ApiRequest
import international.tourism.app.utils.ApiResponse

class BookingService
{
    fun bookingAllData(booking: Booking): ApiResponse
    {
        return ApiRequest.get(ApiRequest.BOOKING_URL.plus("?User_id=").plus(booking.User_id))
    }

    fun bookedHotel(booking: Booking): ApiResponse
    {
        return ApiRequest.get(ApiRequest.BOOKING_URL.plus("?id=").plus(booking.Id))
    }

    fun booking(booking: Booking): ApiResponse
    {
        return ApiRequest.post(ApiRequest.BOOKING_URL,Gson().toJson(booking))
    }

    fun cancelBooking(bookingId: Int): ApiResponse
    {
        return ApiRequest.delete(ApiRequest.BOOKING_URL.plus("?id=$bookingId"))
    }
}