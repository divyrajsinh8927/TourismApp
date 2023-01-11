package international.tourism.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class BookingDetail : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_detail)

        val bookingId = intent.getStringExtra("bookingId")
        Toast.makeText(this,bookingId,Toast.LENGTH_LONG).show()
    }
}