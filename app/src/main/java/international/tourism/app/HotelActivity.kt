package international.tourism.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
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

class HotelActivity : AppCompatActivity()
{
    private lateinit var hotelService: HotelService
    private lateinit var imagesUrl: ImagesUrl
    private lateinit var hotelImage: HotelImage
    private lateinit var hotelImage1: String
    private lateinit var hotelImage2: String
    private lateinit var hotelImage3: String


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel)
        imagesUrl = ImagesUrl()
        val hotelId = intent.getIntExtra("hotelId",0)

        val imageSlider = findViewById<ImageSlider>(R.id.image_slider) // init imageSlider
        val lblPlaceName = findViewById<TextView>(R.id.lblHotel)
        val lblCityName = findViewById<TextView>(R.id.lblCityName)
        val lblCountryName = findViewById<TextView>(R.id.lblCountryName)
        val lblDescription = findViewById<TextView>(R.id.lblHotelDescription)
        val lblPerDayPrice = findViewById<TextView>(R.id.lblPerDayPrise)


        hotelImage = HotelImage(Hotel_id = hotelId)
        val interNetConnection = InterNetConnection()
        if (interNetConnection.checkForInternet(this))
        {
            CoroutineScope(Dispatchers.IO).launch {
                hotelService = HotelService()
                val response = hotelService.getHotelImages(hotelImage)
                if (response.code == HttpURLConnection.HTTP_OK)
                {
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
        } else
        {
            Toast.makeText(this, "Please Connect To Internet!!", Toast.LENGTH_LONG)
                .show()
        }
    }
}