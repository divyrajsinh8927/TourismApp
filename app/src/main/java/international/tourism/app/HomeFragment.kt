package international.tourism.app

import android.content.Intent
import international.tourism.app.adapter.RecHotelAdapter
import international.tourism.app.adapter.PlaceAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import international.tourism.app.models.*
import international.tourism.app.repo.HotelService
import international.tourism.app.repo.PlaceService
import kotlinx.coroutines.*
import java.net.HttpURLConnection

class HomeFragment : Fragment()
{
    private lateinit var placeService: PlaceService
    private lateinit var hotelService: HotelService


    private lateinit var recHomePlace: RecyclerView
    private lateinit var recHomeHotel: RecyclerView
    private lateinit var placeList: ArrayList<Place>
    private lateinit var hotelList: ArrayList<Hotel>
    private lateinit var recPlaceAdapter: PlaceAdapter
    private lateinit var recHotelAdapter: RecHotelAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val imagesUrl = ImagesUrl()

        /* Place Recycle View */
        recHomePlace = view.findViewById(R.id.recHomePlace)
        val interNetConnection = InterNetConnection()
        if (!interNetConnection.checkForInternet(requireContext()))
        {
            Toast.makeText(context, "Please Connect To Internet!", Toast.LENGTH_LONG)
                .show()
            return
        }
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
            GlobalScope.launch(Dispatchers.Main) {

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
                        recPlaceAdapter = PlaceAdapter(
                            requireContext(),
                            placeList,
                            object : PlaceAdapter.OnItemClickListener
                            {
                                override fun onClick(place: Place)
                                {

                                    val intent = Intent(requireContext(), PlaceActivity::class.java)
                                    intent.putExtra("placeId", place.Id)
                                    startActivity(intent)
                                }
                            })

                        recHomePlace.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        recHomePlace.adapter = recPlaceAdapter
                    }
                }
            }
        }


        /* Hotel Recycle View */
        recHomeHotel = view.findViewById(R.id.recHomeHotel)
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
            GlobalScope.launch(Dispatchers.Main) {

                for (hotelKey in hotelData)
                {
                    if (hotelKey.HotelIsDelete == 0)
                    {
                        hotelList.add(
                            Hotel(
                                Id = hotelKey.Id,
                                HotelImage = imagesUrl.ImageBaseUrl.plus(hotelKey.HotelImage),
                                HotelName = hotelKey.HotelName,
                                CityName = hotelKey.CityName
                            )
                        )
                        recHotelAdapter = RecHotelAdapter(
                            requireContext(),
                            hotelList,
                            object : RecHotelAdapter.OnItemClickListener
                            {
                                override fun onClick(hotel: Hotel)
                                {
                                    val intent = Intent(requireContext(), HotelActivity::class.java)
                                    intent.putExtra("hotelId", hotel.Id)
                                    startActivity(intent)
                                }
                            })

                        recHomeHotel.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        recHomeHotel.adapter = recHotelAdapter
                    }
                }
            }
        }
    }
}
