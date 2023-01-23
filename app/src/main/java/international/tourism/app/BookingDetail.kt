package international.tourism.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.view.isVisible
import com.google.gson.Gson
import international.tourism.app.models.Booking
import international.tourism.app.repo.BookingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection

class BookingDetail : AppCompatActivity()
{
    private lateinit var booking: Booking
    private lateinit var bookingService: BookingService

    private lateinit var lblHotelName: TextView
    private lateinit var lblBookingFor: TextView
    private lateinit var lblBookingDate: TextView
    private lateinit var lblArrivalDate: TextView
    private lateinit var lblLeavingDate: TextView
    private lateinit var lblTotalDays: TextView
    private lateinit var lblTotalRooms: TextView
    private lateinit var lblPerDayPrice: TextView
    private lateinit var lblTotalPrice: TextView
    private lateinit var btnCancelBooking: Button

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_detail)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        actionBar.setDisplayHomeAsUpEnabled(true)

        lblHotelName = findViewById(R.id.lblHotelName)
        lblBookingFor = findViewById(R.id.lblBookFor)
        lblBookingDate = findViewById(R.id.lblBookingDate)
        lblArrivalDate = findViewById(R.id.lblArrivalDate)
        lblLeavingDate = findViewById(R.id.lblLeavingDate)
        lblTotalDays = findViewById(R.id.lblTotalDays)
        lblTotalRooms = findViewById(R.id.lblTotalRooms)
        lblPerDayPrice = findViewById(R.id.lblPerDayPrice)
        lblTotalPrice = findViewById(R.id.lblTotalPrice)
        btnCancelBooking = findViewById(R.id.btnCancelBooking)


        val bookingId = intent.getIntExtra("bookingId", 0)
        booking = Booking(Id = bookingId)


        val interNetConnection = InterNetConnection()
        if (!interNetConnection.checkForInternet(this))
        {
            Toast.makeText(this, "Please Connect To Internet!!", Toast.LENGTH_LONG)
                .show()
            return
        }
        configureData()
    }

    private fun configureData()
    {

        CoroutineScope(Dispatchers.IO).launch {
            bookingService = BookingService()

            val response = bookingService.bookedHotel(booking)
            if (response.code == HttpURLConnection.HTTP_NOT_FOUND)
            {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@BookingDetail, "No Booking History!!", Toast.LENGTH_LONG)
                        .show()
                }
            }

            val bookingData = Gson().fromJson(response.message, Array<Booking>::class.java)

            withContext(Dispatchers.Main) {
                for (bookings in bookingData)
                {
                    lblHotelName.text = bookings.HotelName
                    lblBookingFor.text = bookings.BookingFor
                    lblBookingDate.text = bookings.BookingDate
                    lblArrivalDate.text = bookings.ArrivalDate
                    lblLeavingDate.text = bookings.LeavingDate
                    lblTotalDays.text = bookings.Totaldays.toString().plus(" Day's")
                    lblTotalRooms.text = bookings.TotalRooms.toString().plus(" Room's")
                    lblPerDayPrice.text = bookings.PerDayPrice.toString().plus(" Rs")
                    lblTotalPrice.text = bookings.TotalPrice.toString().plus(" Rs")

                    if (bookings.Status != "waiting")
                    {
                        btnCancelBooking.isVisible = false
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean
    {
        finish()
        return true
    }

}