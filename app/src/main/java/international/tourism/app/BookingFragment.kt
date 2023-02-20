package international.tourism.app

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import international.tourism.app.adapter.BookingAdapter
import international.tourism.app.models.Booking
import international.tourism.app.services.BookingService
import kotlinx.coroutines.*
import java.net.HttpURLConnection

class BookingFragment : Fragment()
{

    private lateinit var bookingService: BookingService
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var booking: Booking
    private lateinit var bookingList: ArrayList<Booking>
    private lateinit var bookingRecycledView: RecyclerView
    private lateinit var bookingAdapter: BookingAdapter
    private lateinit var attachedContext: Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        bookingRecycledView = view.findViewById(R.id.lstBooking)
        attachedContext = activity ?: return

        val userId = checkLogin()

        if (userId == 0)
            return

        booking = Booking(User_id = userId)

        populateBookingData()
        bookingRecycledView.startLayoutAnimation()

    }

    private fun checkLogin(): Int
    {
        val interNetConnection = InterNetConnection()
        if (!interNetConnection.checkForInternet(attachedContext))
        {
            Toast.makeText(context, "Please Connect To Internet!!", Toast.LENGTH_LONG)
                .show()
            return 0
        }

        sharedPreferences = this.attachedContext
            .getSharedPreferences("tourism_pref", AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPreferences.getString("id", null)
        if (userId == null)
        {
            Toast.makeText(attachedContext, "Please Login First!!", Toast.LENGTH_LONG)
                .show()
            startActivity(Intent(attachedContext, LoginActivity::class.java))
            return 0
        }

        return userId.toInt()
    }

    private fun populateBookingData()
    {
        CoroutineScope(Dispatchers.IO).launch {
            bookingService = BookingService()

            val response = bookingService.bookingAllData(booking)
            if (response.code == HttpURLConnection.HTTP_NOT_FOUND)
            {
                Toast.makeText(attachedContext, "No Booking History!!", Toast.LENGTH_LONG)
                    .show()
                return@launch
            }

            bookingList = ArrayList()
            val bookingData =
                Gson().fromJson(response.message, Array<Booking>::class.java)
            for (booking in bookingData)
            {

                withContext(Dispatchers.Main) {
                    bookingList.add(
                        Booking(
                            Id = booking.Id,
                            HotelName = booking.HotelName,
                            TotalPrice = booking.TotalPrice,
                            Status = booking.Status
                        )
                    )
                    bookingAdapter = BookingAdapter(
                        attachedContext,
                        bookingList,
                        object : BookingAdapter.OnItemClickListener
                        {
                            override fun onClick(bookings: Booking)
                            {
                                val intent = Intent(attachedContext, BookingDetail::class.java)
                                intent.putExtra("bookingId", bookings.Id)
                                startActivity(intent)
                            }
                        })
                    bookingRecycledView.layoutManager = GridLayoutManager(attachedContext, 1)
                    bookingRecycledView.adapter = bookingAdapter
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("Fragment",4)
    }
}
