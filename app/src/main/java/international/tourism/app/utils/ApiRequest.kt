package international.tourism.app.utils

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ApiRequest
{
    //home : 192.168.0.102
    //mobile: 192.168.145.51
    companion object
    {
        @JvmStatic val BASE_URL = "http://192.168.43.23/ATourism/api"
        @JvmStatic val LOGIN_URL = "$BASE_URL/login.php"
        @JvmStatic val REGISTER_URL = "$BASE_URL/user.php"
        @JvmStatic val COUNTRY_URL = "$BASE_URL/country.php"
        @JvmStatic val PLACE_URL = "$BASE_URL/place.php"
        @JvmStatic val HOTEL_URL = "$BASE_URL/hotel.php"
        @JvmStatic val BOOKING_URL = "$BASE_URL/booking.php"
        @JvmStatic val PLACE_IMAGE = "$BASE_URL/placeimages.php"


        @JvmStatic
        fun get(url: String): ApiResponse
        {
            return send(Request.Builder().url(url).build())
        }

        @JvmStatic
        fun post(url: String, body: String): ApiResponse
        {
            return send(Request.Builder().url(url).post(body.toRequestBody()).build())
        }

        @JvmStatic
        fun put(url: String, body: String): ApiResponse
        {
            return send(Request.Builder().url(url).put(body.toRequestBody()).build())
        }

        @JvmStatic
        fun delete(url: String): ApiResponse
        {
            return send(Request.Builder().url(url).delete().build())
        }

        @JvmStatic
        private fun send(request: Request): ApiResponse {
            val client = OkHttpClient()
            val response = client.newCall(request).execute()

            return ApiResponse(
                code = response.code,
                message = response.body!!.string()
            )
        }
    }
}