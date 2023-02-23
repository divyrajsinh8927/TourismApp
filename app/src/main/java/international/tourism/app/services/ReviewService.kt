package international.tourism.app.services
import international.tourism.app.utils.ApiRequest
import international.tourism.app.utils.ApiResponse

class ReviewService
{
    fun getAllReview(): ApiResponse
    {
        return ApiRequest.get(ApiRequest.REVIEW_URl)
    }
}