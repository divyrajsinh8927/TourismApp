package international.tourism.app

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ActionTypes
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemChangeListener
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.interfaces.TouchListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import international.tourism.app.models.Booking
import international.tourism.app.models.HotelImage
import international.tourism.app.models.ImagesUrl
import international.tourism.app.services.BookingService
import international.tourism.app.services.HotelService
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HotelActivity : AppCompatActivity()
{
    private lateinit var bookingService: BookingService
    private lateinit var booking: Booking
    private lateinit var hotelService: HotelService
    private lateinit var imagesUrl: ImagesUrl
    private lateinit var hotelImage: HotelImage
    private lateinit var hotelImage1: String
    private lateinit var hotelImage2: String
    private lateinit var hotelImage3: String
    private lateinit var imageSlider: ImageSlider// init imageSlider
    private lateinit var lblPlaceName: TextView
    private lateinit var lblCityName: TextView
    private lateinit var lblCountryName: TextView
    private lateinit var lblDescription: TextView
    private lateinit var lblPerDayPrice: TextView
    private lateinit var txtTotalRooms: EditText
    private lateinit var txtBookFor: EditText
    private lateinit var bookingFor: String
    private var totalDays: Int = 0
    private var totalPrice: Int = 0
    private var perDayPrice = 0
    private var totalRooms = 1
    private var hotelId: Int = 0
    private lateinit var txtTotalDays: TextView
    private lateinit var lblArrivalDate: TextView
    private lateinit var lblLeavingDate: TextView
    private lateinit var txtTotalPrice: TextView
    private lateinit var btnBooking: Button
    private lateinit var arrivalDate: String
    private lateinit var leavingDate: String

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar!!.setCustomView(R.layout.toolbar_title_layout)
        supportActionBar!!.elevation = 0F
        setContentView(R.layout.activity_hotel)

        lblArrivalDate = findViewById(R.id.lblArrivalDate)
        lblLeavingDate = findViewById(R.id.lblLeavingDate)
        btnBooking = findViewById(R.id.btnBooking)

        findViewById<ConstraintLayout>(R.id.dtArrivalDate).setOnClickListener {
             showArrivalCalender()
        }

        findViewById<ConstraintLayout>(R.id.dtLeavingDate).setOnClickListener{
            showLeavingCalender()
        }

        btnBooking.setOnClickListener { bookingHotel() }

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        actionBar.setDisplayHomeAsUpEnabled(true)
        val interNetConnection = InterNetConnection()
        if (!interNetConnection.checkForInternet(this))
        {
            Toast.makeText(this, "Please Connect To Internet!!", Toast.LENGTH_LONG)
                .show()
            return
        }
        imagesUrl = ImagesUrl()
        hotelId = intent.getIntExtra("hotelId", 0)

        imageSlider = findViewById(R.id.image_slider) // init imageSlider
        lblPlaceName = findViewById(R.id.lblHotel)
        lblCityName = findViewById(R.id.lblCityName)
        lblCountryName = findViewById(R.id.lblCountryName)
        lblDescription = findViewById(R.id.lblHotelDescription)
        lblPerDayPrice = findViewById(R.id.lblPerDayPrise)
        txtTotalDays = findViewById(R.id.txtTotalDays)
        txtTotalRooms = findViewById(R.id.txtTotalRooms)
        txtTotalRooms.addTextChangedListener(textWatcher)
        txtTotalPrice = findViewById(R.id.txtTotalPrice)
        txtBookFor = findViewById(R.id.txtBookFor)



        hotelImage = HotelImage(Hotel_id = hotelId)
        populateHotelData()

    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s?.toString() == "")
            {
                totalRooms = 1
                return
            }
            else
            {
            totalRooms = s.toString().toInt()
            }

            calculateTotalPrice()
        }
    }

    private fun populateHotelData()
    {
        CoroutineScope(Dispatchers.IO).launch {
            hotelService = HotelService()
            val response = hotelService.getHotelImages(hotelImage)
            if (response.code == HttpURLConnection.HTTP_NOT_FOUND)
            {
                Toast.makeText(this@HotelActivity, "No Data Found!", Toast.LENGTH_LONG)
                    .show()
            }
            val data = Gson().fromJson(response.message, Array<HotelImage>::class.java)
            withContext(Dispatchers.Main) {
                for (key in data)
                {
                    hotelImage1 = key.HotelImage1
                    hotelImage2 = key.HotelImage2
                    hotelImage3 = key.HotelImage3
                    lblPlaceName.text = key.HotelName
                    lblCityName.text = key.CityName
                    lblCountryName.text = key.CountryName
                    lblDescription.text = key.Discription
                    perDayPrice = key.PerDayPrice
                    lblPerDayPrice.text = key.PerDayPrice.toString().plus(" Rs")
                }
                val imageList = ArrayList<SlideModel>()
                imageList.add(SlideModel(imagesUrl.ImageBaseUrl.plus(hotelImage1), ScaleTypes.FIT))
                imageList.add(SlideModel(imagesUrl.ImageBaseUrl.plus(hotelImage2), ScaleTypes.FIT))
                imageList.add(SlideModel(imagesUrl.ImageBaseUrl.plus(hotelImage3), ScaleTypes.FIT))
                imageSlider.setImageList(imageList)

                imageSlider.setItemClickListener(object : ItemClickListener
                {
                    override fun onItemSelected(position: Int)
                    {
                        // You can listen here.
                    }
                })

                imageSlider.setItemChangeListener(object : ItemChangeListener
                {
                    override fun onItemChanged(position: Int)
                    {
                        //println("Pos: " + position)
                    }
                })

                imageSlider.setTouchListener(object : TouchListener
                {
                    override fun onTouched(touched: ActionTypes)
                    {
                        if (touched == ActionTypes.DOWN)
                        {
                            imageSlider.stopSliding()
                        } else if (touched == ActionTypes.UP)
                        {
                            imageSlider.startSliding(5000)
                        }
                    }
                })

            }
        }
    }

    override fun onSupportNavigateUp(): Boolean
    {
        finish()
        return true
    }

    private fun showArrivalCalender()
    {
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            this,
            { _, calendarYear, monthOfYear, dayOfMonth ->

                val calendar = Calendar.getInstance()

                calendar.set(calendarYear, monthOfYear, dayOfMonth)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                val dateFormatDatabase = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                lblArrivalDate.text = dateFormat.format(calendar.time)
                arrivalDate = dateFormatDatabase.format(calendar.time)

            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun showLeavingCalender()
    {
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            this,
            { _, calendarYear, monthOfYear, dayOfMonth ->

                val calendar = Calendar.getInstance()
                calendar.set(calendarYear, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                val dateFormatDatabase = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                lblLeavingDate.text = dateFormat.format(calendar.time)
                leavingDate = dateFormatDatabase.format(calendar.time)
                calculateTotalDay()
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun calculateTotalDay()
    {
        val date1 = lblArrivalDate.text.toString()
        val date2 = lblLeavingDate.text.toString()

        val mDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)

        val mDate11 = mDateFormat.parse(date1)
        val mDate22 = mDateFormat.parse(date2)

        val mDifference = kotlin.math.abs(mDate11!!.time - mDate22!!.time)
        val mDifferenceDates = (mDifference / (24 * 60 * 60 * 1000)) + 1

        totalDays = mDifferenceDates.toInt()
        txtTotalDays.text = totalDays.toString()

        calculateTotalPrice()
    }

    private fun calculateTotalPrice()
    {
        totalPrice = totalDays * perDayPrice * totalRooms
        txtTotalPrice.text = totalPrice.toString()
    }

    private fun bookingHotel()
    {
        bookingFor = txtBookFor.text.toString()
        val currentDate = Calendar.getInstance()
        val dateFormatDatabase = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val bookingDate = dateFormatDatabase.format(currentDate.time)
        val sharedPref = getSharedPreferences("tourism_pref", MODE_PRIVATE)
        val userId = sharedPref.getString("id", null)
        booking = Booking(
                            BookingFor = bookingFor,
                            HotelName = hotelId.toString(),
                            BookingDate = bookingDate,
                            ArrivalDate = arrivalDate,
                            LeavingDate = leavingDate,
                            Totaldays = totalDays,
                            TotalRooms = totalRooms,
                            TotalPrice = totalPrice,
                            User_id = userId.toString().toInt()
                         )
        CoroutineScope(Dispatchers.IO).launch {
            bookingService = BookingService()
            val response = bookingService.booking(booking)
            if (response.code == HttpURLConnection.HTTP_CREATED)
            {
                Looper.prepare()
                Toast.makeText(this@HotelActivity, "Booking Done!", Toast.LENGTH_LONG)
                    .show()
                Looper.loop()
                return@launch
            }
            Looper.prepare()
            Toast.makeText(this@HotelActivity, "Something Wrong!", Toast.LENGTH_LONG)
                .show()
            Looper.loop()
        }

    }
}