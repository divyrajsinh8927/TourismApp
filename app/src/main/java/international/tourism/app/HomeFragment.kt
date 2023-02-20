package international.tourism.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import international.tourism.app.adapter.HomeHotelAdapter
import international.tourism.app.adapter.HomePlaceAdapter
import international.tourism.app.models.*
import international.tourism.app.services.HotelService
import international.tourism.app.services.PlaceService
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class HomeFragment : Fragment()
{
    private lateinit var placeService: PlaceService
    private lateinit var hotelService: HotelService
    private lateinit var btnPlaceViewAll: Button
    private lateinit var btnViewAllHotel: Button
    private lateinit var bottomNavigationView: ChipNavigationBar
    private lateinit var imagesUrl: ImagesUrl
    private lateinit var recHomePlace: RecyclerView
    private lateinit var recHomeHotel: RecyclerView
    private lateinit var placeList: ArrayList<Place>
    private lateinit var hotelList: ArrayList<Hotel>
    private lateinit var homePlaceAdapter: HomePlaceAdapter
    private lateinit var homeHotelAdapter: HomeHotelAdapter
    private lateinit var attachedContext: Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        attachedContext = activity ?: return

        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavBar)
        btnPlaceViewAll = view.findViewById(R.id.btnViewAllPlace)
        btnViewAllHotel = view.findViewById(R.id.btnViewAllHotel)
        btnPlaceViewAll.setOnClickListener {
            (requireActivity() as MainActivity).showPlaceFragment()
        }
        btnViewAllHotel.setOnClickListener {
            (requireActivity() as MainActivity).showHotelFragment()
        }
        val interNetConnection = InterNetConnection()
        if (!interNetConnection.checkForInternet(attachedContext))
        {
            Toast.makeText(context, "Please Connect To Internet!", Toast.LENGTH_LONG)
                .show()
            return
        }
        imagesUrl = ImagesUrl()
        /* Place Recycle View */
        recHomePlace = view.findViewById(R.id.recHomePlace)
        recHomePlace.setHasFixedSize(true)
        populatePlaceData()
        recHomePlace.startLayoutAnimation()


        /* Hotel Recycle View */
        recHomeHotel = view.findViewById(R.id.recHomeHotel)
        populateHotelData()
        recHomeHotel.startLayoutAnimation()

    }

    private fun populatePlaceData()
    {
        CoroutineScope(Dispatchers.IO).launch {
            placeService = PlaceService()
            val response = placeService.getAllPlace()
            if (response.code == HttpURLConnection.HTTP_NOT_FOUND)
            {
                Toast.makeText(context, "No Data Found!", Toast.LENGTH_LONG)
                    .show()
            }
            placeList = ArrayList()
            val placeData = Gson().fromJson(response.message, Array<Place>::class.java)
            withContext(Dispatchers.Main) {

                for (place in placeData)
                {
                    if (place.PlaceIsDelete == 0)
                    {
                        placeList.add(
                            Place(
                                Id = place.Id,
                                PlaceImage = imagesUrl.ImageBaseUrl.plus(place.PlaceImage),
                                PlaceName = place.PlaceName,
                                CityName = place.CityName
                            )
                        )
                        homePlaceAdapter = HomePlaceAdapter(
                            attachedContext,
                            placeList,
                            object : HomePlaceAdapter.OnItemClickListener
                            {
                                override fun onClick(place: Place)
                                {

                                    val intent = Intent(attachedContext, PlaceActivity::class.java)
                                    intent.putExtra("placeId", place.Id)
                                    startActivity(intent)
                                }
                            })

                        recHomePlace.layoutManager =
                            GridLayoutManager(attachedContext, 2)
                        recHomePlace.adapter = homePlaceAdapter
                    }
                }
            }
        }

    }

    private fun populateHotelData()
    {
        CoroutineScope(Dispatchers.IO).launch {
            hotelService = HotelService()
            val hotelResponse = hotelService.getAllHotel()
            if (hotelResponse.code == HttpURLConnection.HTTP_NOT_FOUND)
            {
                Toast.makeText(context, "No Data Found!", Toast.LENGTH_LONG)
                    .show()
            }
            hotelList = ArrayList()
            val hotelData = Gson().fromJson(hotelResponse.message, Array<Hotel>::class.java)
            withContext(Dispatchers.Main) {

                for (hotelKey in hotelData)
                {
                    if (hotelKey.HotelIsDelete == 1)
                        continue

                    hotelList.add(
                        Hotel(
                            Id = hotelKey.Id,
                            HotelImage = imagesUrl.ImageBaseUrl.plus(hotelKey.HotelImage),
                            HotelName = hotelKey.HotelName,
                            CityName = hotelKey.CityName
                        )
                    )
                    homeHotelAdapter = HomeHotelAdapter(
                        attachedContext,
                        hotelList,
                        object : HomeHotelAdapter.OnItemClickListener
                        {
                            override fun onClick(hotel: Hotel)
                            {
                                val intent = Intent(attachedContext, HotelActivity::class.java)
                                intent.putExtra("hotelId", hotel.Id)
                                startActivity(intent)
                            }
                        })

                    recHomeHotel.layoutManager =
                        GridLayoutManager(attachedContext, 2)
                    recHomeHotel.adapter = homeHotelAdapter

                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("Fragment",1)
    }
}
