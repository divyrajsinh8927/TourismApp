package international.tourism.app

import international.tourism.app.adapter.recHotelAdapter
import international.tourism.app.adapter.recPlaceAdapter
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
import international.tourism.app.repo.AuthService
import kotlinx.coroutines.*
import java.net.HttpURLConnection

class HomeFragment : Fragment()
{
    private lateinit var authService: AuthService


    private lateinit var recHomePlace: RecyclerView
    private lateinit var recHomeHotel: RecyclerView
    private lateinit var recPlaceModel: ArrayList<recPlacemodel>
    private lateinit var recHotelModel: ArrayList<recHotelmodel>
    private lateinit var recPlaceAdapter: recPlaceAdapter
    private lateinit var recHotelAdapter: recHotelAdapter

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

        /* Place Recycle View */
        recHomePlace = view.findViewById(R.id.recHomePlace)
        val interNetConnection = InterNetConnection()
        if (interNetConnection.checkForInternet(requireContext()))
        {
            CoroutineScope(Dispatchers.IO).launch {
                authService = AuthService()
                val response = authService.getAllPlace()
                if (response.code == HttpURLConnection.HTTP_OK)
                {
                    recPlaceModel = ArrayList()
                    val data = Gson().fromJson(response.message, Array<Place>::class.java)
                    GlobalScope.launch(Dispatchers.Main) {

                        for (key in data)
                        {
                            if (key.PlaceIsDelete == 0)
                            {
                                recPlaceModel.add(
                                    recPlacemodel(
                                        "http://192.168.43.23/ATourism/images/".plus(key.PlaceImage),
                                        key.PlaceName,
                                        key.CityName
                                    )
                                )
                            }
                        }
                        recPlaceAdapter = context?.let { recPlaceAdapter(it, recPlaceModel) }!!
                        val manager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        recHomePlace.layoutManager = manager
                        recHomePlace.adapter = recPlaceAdapter
                        recHomePlace.layoutManager = manager
                    }
                }
            }


            /* Hotel Recycle View */
            recHomeHotel = view.findViewById(R.id.recHomeHotel)
            CoroutineScope(Dispatchers.IO).launch {
                authService = AuthService()
                val hotelResponse = authService.getAllHotel()
                if (hotelResponse.code == HttpURLConnection.HTTP_OK)
                {
                    recHotelModel = ArrayList()
                    val hotelData = Gson().fromJson(hotelResponse.message, Array<Hotel>::class.java)
                    GlobalScope.launch(Dispatchers.Main) {

                        for (hotelKey in hotelData)
                        {
                            if (hotelKey.HotelIsDelete == 0)
                            {
                                recHotelModel.add(
                                    recHotelmodel(
                                        "http://192.168.43.23/ATourism/images/".plus(hotelKey.HotelImage),
                                        hotelKey.HotelName,
                                        hotelKey.CityName
                                    )
                                )
                            }
                        }

                        recHotelAdapter = context?.let { recHotelAdapter(it, recHotelModel) }!!
                        val hotelManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        recHomeHotel.layoutManager = hotelManager
                        recHomeHotel.adapter = recHotelAdapter
                        recHomeHotel.layoutManager = hotelManager
                    }
                }
            }
        }else
        {
            Toast.makeText(context, "Please Connect To Internet!", Toast.LENGTH_LONG)
                .show()
        }
    }
}
