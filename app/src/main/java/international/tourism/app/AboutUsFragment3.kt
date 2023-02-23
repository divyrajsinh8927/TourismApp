package international.tourism.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import international.tourism.app.adapter.RecHotelAdapter
import international.tourism.app.adapter.ReviewsAdapter
import international.tourism.app.models.Hotel
import international.tourism.app.models.ImagesUrl
import international.tourism.app.models.Review
import international.tourism.app.services.HotelService
import international.tourism.app.services.ReviewService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection

class AboutUsFragment3 : Fragment()
{
    private lateinit var ReviewService: ReviewService
    private lateinit var attachedContext: Activity
    private lateinit var recReview: RecyclerView
    private lateinit var reviewList: ArrayList<Review>
    private lateinit var reviewsAdapter: ReviewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_us_3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

//        val lblUserName = view.findViewById<TextView>(R.id.lblUserName)
//        val rBar = view.findViewById<RatingBar>(R.id.userRating)

//        val msg = rBar.rating.toString()
//        Toast.makeText(context,
//            "Rating is: " + msg, Toast.LENGTH_SHORT).show()


        super.onViewCreated(view, savedInstanceState)
        attachedContext = activity ?: return

        recReview = view.findViewById(R.id.recReview)

        val interNetConnection = InterNetConnection()
        if (!interNetConnection.checkForInternet(attachedContext))
        {
            Toast.makeText(context, "Please Connect To Internet!!", Toast.LENGTH_LONG)
                .show()
            return
        }
        populateReviewData()
        recReview.startLayoutAnimation()
    }
    private fun populateReviewData()
    {
        CoroutineScope(Dispatchers.IO).launch {
            ReviewService = ReviewService()
            val ReviewResponse = ReviewService.getAllReview()
            if (ReviewResponse.code == HttpURLConnection.HTTP_NOT_FOUND)
            {
                Toast.makeText(context, "No Data Found!!", Toast.LENGTH_LONG)
                    .show()
                return@launch
            }
            reviewList = ArrayList()
            val ReviewlData = Gson().fromJson(ReviewResponse.message, Array<Review>::class.java)
            withContext(Dispatchers.Main) {

                for (ReviewKey in ReviewlData)
                {
                    reviewList.add(
                        Review(
                            Id = ReviewKey.Id,
                            Reviews = ReviewKey.Reviews,
                            Stars = ReviewKey.Stars,
                            userName = ReviewKey.userName
                        )
                    )
                    reviewsAdapter = ReviewsAdapter(
                        attachedContext.applicationContext,
                        reviewList,
                        object : ReviewsAdapter.OnItemClickListener
                        {
                            override fun onClick(review: Review)
                            {

                            }
                        })
                    recReview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true)
                    recReview.adapter = reviewsAdapter
                }
            }
        }
    }
}