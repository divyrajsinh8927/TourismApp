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
import international.tourism.app.services.BookingService
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
    private var bookingId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar!!.setCustomView(R.layout.toolbar_title_layout)
        supportActionBar!!.elevation = 0F
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


        bookingId = intent.getIntExtra("bookingId", 0)
        booking = Booking(Id = bookingId)


        val interNetConnection = InterNetConnection()
        if (!interNetConnection.checkForInternet(this))
        {
            Toast.makeText(this, "Please Connect To Internet!!", Toast.LENGTH_LONG)
                .show()
            return
        }

        populateBookingData()
        btnCancelBooking.setOnClickListener { cancelBooking() }
    }

    private fun populateBookingData()
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
                for (booking in bookingData)
                {
                    lblHotelName.text = booking.HotelName
                    lblBookingFor.text = booking.BookingFor
                    lblBookingDate.text = booking.BookingDate
                    lblArrivalDate.text = booking.ArrivalDate
                    lblLeavingDate.text = booking.LeavingDate
                    lblTotalDays.text = booking.Totaldays.toString().plus(" Day's")
                    lblTotalRooms.text = booking.TotalRooms.toString().plus(" Room's")
                    lblPerDayPrice.text = booking.PerDayPrice.toString().plus(" Rs")
                    lblTotalPrice.text = booking.TotalPrice.toString().plus(" Rs")

                    if (booking.Status != "waiting" || booking.BookingIsCancel == 1)
                        btnCancelBooking.isVisible = false
                }
            }
        }
    }

    private fun cancelBooking()
    {
        CoroutineScope(Dispatchers.IO).launch {
            val cancelBookingResponse = bookingService.cancelBooking(bookingId)
            if(cancelBookingResponse.code == HttpURLConnection.HTTP_OK)
            {
                withContext(Dispatchers.Main)
                {
                    Toast.makeText(this@BookingDetail, "Booking IS Canceled", Toast.LENGTH_SHORT).show()
                    btnCancelBooking.isVisible = false
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