package international.tourism.app

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking, container, false)

    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val listView = requireView().findViewById<ListView>(R.id.lstBooking)
        sharedPreferences = this.requireActivity()
            .getSharedPreferences("tourism_pref", AppCompatActivity.MODE_PRIVATE)
        val userId = sharedPreferences.getString("id", null)
        if (userId == null)
        {
            Toast.makeText(context, "Please Login First!!", Toast.LENGTH_LONG)
                .show()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            return
        }
        booking = Booking(User_id = userId.toInt())
        val interNetConnection = InterNetConnection()
        if (!interNetConnection.checkForInternet(requireContext()))
        {
            Toast.makeText(context, "Please Connect To Internet!!", Toast.LENGTH_LONG)
                .show()
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            bookingService = BookingService()

            val response = bookingService.bookingAllData(booking)
            if (response.code == HttpURLConnection.HTTP_NOT_FOUND)
            {
                Toast.makeText(context, "No Booking History!!", Toast.LENGTH_LONG)
                    .show()
            }


            val bookingData =
                Gson().fromJson(response.message, Array<Booking>::class.java)
            GlobalScope.launch(Dispatchers.Main) {
                val adapter = BookingAdapter(requireActivity(), bookingData)
                listView.adapter = adapter

                listView.setOnItemClickListener { _, _, position, _ ->
                    val bookingId = bookingData[position].Id
                    val intent = Intent(requireContext(), BookingDetail::class.java)
                    intent.putExtra("bookingId", bookingId)
                    startActivity(intent)
                }
            }
        }
    }
}