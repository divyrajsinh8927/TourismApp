package international.tourism.app

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import international.tourism.app.models.HotelImage
import international.tourism.app.models.ImagesUrl
import international.tourism.app.repo.HotelService
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.util.*
import kotlin.collections.ArrayList

@Suppress("NAME_SHADOWING", "CAST_NEVER_SUCCEEDS")
class HotelActivity : AppCompatActivity()
{
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
    private var totalDays: Long = 0
    private lateinit var txtTotalDays: TextView
    private lateinit var lblArrivalDate: TextView
    private lateinit var lblLeavingDate: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel)

        lblArrivalDate = findViewById(R.id.lblArrivalDate)
        lblLeavingDate = findViewById(R.id.lblLeavingDate)

        findViewById<ConstraintLayout>(R.id.dtArrivalDate).setOnClickListener {
             showArrivalCalender()
        }

        findViewById<ConstraintLayout>(R.id.dtLeavingDate).setOnClickListener{
            showLeavingCalender()
        }


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
        val hotelId = intent.getIntExtra("hotelId", 0)

        imageSlider = findViewById(R.id.image_slider) // init imageSlider
        lblPlaceName = findViewById(R.id.lblHotel)
        lblCityName = findViewById(R.id.lblCityName)
        lblCountryName = findViewById(R.id.lblCountryName)
        lblDescription = findViewById(R.id.lblHotelDescription)
        lblPerDayPrice = findViewById(R.id.lblPerDayPrise)
        txtTotalDays = findViewById(R.id.txtTotalDays)


        hotelImage = HotelImage(Hotel_id = hotelId)
        configureHotelData()

    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateTotalDay()
    {
        val date1 = lblArrivalDate.text.toString()
        val date2 = lblLeavingDate.text.toString()

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun configureHotelData()
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
            GlobalScope.launch(Dispatchers.Main) {
                for (key in data)
                {
                    hotelImage1 = key.HotelImage1
                    hotelImage2 = key.HotelImage2
                    hotelImage3 = key.HotelImage3
                    lblPlaceName.text = key.HotelName
                    lblCityName.text = key.CityName
                    lblCountryName.text = key.CountryName
                    lblDescription.text = key.Discription
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
            { _, year, monthOfYear, dayOfMonth ->

                   lblArrivalDate.text = (year.toString().plus( "-").plus(monthOfYear + 1).plus( "-").plus(dayOfMonth.toString()))
            },
            year,
            month,
            day
        )
        // at last we are calling show
        // to display our date picker dialog.
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showLeavingCalender()
    {
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->

                lblLeavingDate.text = (year.toString().plus( "-").plus(monthOfYear + 1).plus( "-").plus(dayOfMonth.toString()))
            },
            year,
            month,
            day
        )
        // at last we are calling show
        // to display our date picker dialog.
        datePickerDialog.show()
        calculateTotalDay()
    }
}