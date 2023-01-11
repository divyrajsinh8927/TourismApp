package international.tourism.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ActionTypes
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemChangeListener
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.interfaces.TouchListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import international.tourism.app.models.ImagesUrl
import international.tourism.app.models.PlaceImage
import international.tourism.app.repo.PlaceService
import kotlinx.coroutines.*
import java.net.HttpURLConnection

class PlaceActivity : AppCompatActivity()
{
    private lateinit var placeService: PlaceService
    private lateinit var placeImage: PlaceImage
    private lateinit var placeImage1: String
    private lateinit var placeImage2: String
    private lateinit var placeImage3: String


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        actionBar.setDisplayHomeAsUpEnabled(true)

        val placeId : String = intent.getIntExtra("placeId", 0).toString()

        val imageSlider = findViewById<ImageSlider>(R.id.image_slider) // init imageSlider
        val lblPlaceName = findViewById<TextView>(R.id.lblPlace)
        val lblCityName = findViewById<TextView>(R.id.lblCityName)
        val lblCountryName = findViewById<TextView>(R.id.lblCountryName)
        val lblDescription = findViewById<TextView>(R.id.lblPlaceDescription)

        val imagesUrl = ImagesUrl()
        placeImage = PlaceImage(Place_id = placeId.toInt())

        val interNetConnection = InterNetConnection()
        if (interNetConnection.checkForInternet(this))
        {
            CoroutineScope(Dispatchers.IO).launch {
                placeService = PlaceService()
                val response = placeService.getPlaceImages(placeImage)
                if (response.code == HttpURLConnection.HTTP_OK)
                {
                    val placeData = Gson().fromJson(response.message, Array<PlaceImage>::class.java)
                    GlobalScope.launch(Dispatchers.Main) {
                    for (place in placeData)
                    {
                        placeImage1 = place.PlaceImage1
                        placeImage2 = place.PlaceImage2
                        placeImage3 = place.PlaceImage3
                        lblPlaceName.text = place.PlaceName
                        lblCityName.text = place.CityName
                        lblCountryName.text = place.CountryName
                        lblDescription.text =place.Discription
                    }
                        val imageList = ArrayList<SlideModel>()
                        imageList.add(SlideModel(imagesUrl.ImageBaseUrl.plus(placeImage1), ScaleTypes.FIT))
                        imageList.add(SlideModel(imagesUrl.ImageBaseUrl.plus(placeImage2), ScaleTypes.FIT))
                        imageList.add(SlideModel(imagesUrl.ImageBaseUrl.plus(placeImage3), ScaleTypes.FIT))
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
