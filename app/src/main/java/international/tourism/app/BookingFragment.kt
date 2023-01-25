package international.tourism.app

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
import international.tourism.app.repo.BookingService
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
        bookingRecycledView = requireView().findViewById(R.id.lstBooking)

        val userId = checkLogin()

        if(userId == 0)
            return

        booking = Booking(User_id = userId)

        configureData()
        bookingRecycledView.startLayoutAnimation()

    }

    private fun checkLogin(): Int
    {
        val interNetConnection = InterNetConnection()
        if (!interNetConnection.checkForInternet(requireContext()))
        {
            Toast.makeText(context, "Please Connect To Internet!!", Toast.LENGTH_LONG)
                .show()
            return 0
        }

        sharedPreferences = this.requireActivity()
            .getSharedPreferences("tourism_pref", AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPreferences.getString("id", null)
        if (userId == null)
        {
            Toast.makeText(context, "Please Login First!!", Toast.LENGTH_LONG)
                .show()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            return 0
        }

        return userId.toInt()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun configureData()
    {
        CoroutineScope(Dispatchers.IO).launch {
            bookingService = BookingService()

            val response = bookingService.bookingAllData(booking)
            if (response.code == HttpURLConnection.HTTP_NOT_FOUND)
            {
                Toast.makeText(context, "No Booking History!!", Toast.LENGTH_LONG)
                    .show()
                return@launch
            }

            bookingList = ArrayList()
            val bookingData =
                Gson().fromJson(response.message, Array<Booking>::class.java)
            GlobalScope.launch(Dispatchers.Main) {
                for (booking in bookingData)
                {

                    GlobalScope.launch(Dispatchers.Main) {
                        bookingList.add(
                            Booking(
                                Id = booking.Id,
                                HotelName = booking.HotelName,
                                TotalPrice = booking.TotalPrice,
                                Status = booking.Status
                            )
                        )
                         bookingAdapter = BookingAdapter(requireContext(), bookingList, object: BookingAdapter.OnItemClickListener
                        {
                            override fun onClick(bookings: Booking)
                            {
                                val intent = Intent(requireContext(), BookingDetail::class.java)
                                intent.putExtra("bookingId", bookings.Id)
                                startActivity(intent)
                            }
                        })
                        bookingRecycledView.layoutManager = GridLayoutManager(requireContext(), 1)
                        bookingRecycledView.adapter = bookingAdapter
                    }
                }
            }
        }
    }
}