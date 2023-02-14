package international.tourism.app.services

import com.google.gson.Gson
import international.tourism.app.models.PlaceImage
import international.tourism.app.utils.ApiRequest
import international.tourism.app.utils.ApiResponse

class PlaceService
{
    fun getAllPlace(): ApiResponse
    {
        return ApiRequest.get(ApiRequest.PLACE_URL)
    }

    fun getPlaceImages(placeImage: PlaceImage): ApiResponse
    {
        return ApiRequest.post(ApiRequest.PLACE_URL, Gson().toJson(placeImage))
    }
}