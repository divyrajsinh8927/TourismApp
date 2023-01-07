package international.tourism.app

import international.tourism.app.adapter.recHotelAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import international.tourism.app.models.Hotel
import international.tourism.app.models.recHotelmodel
import international.tourism.app.repo.AuthService
import kotlinx.coroutines.*
import java.net.HttpURLConnection

class HotelFragment : Fragment()
{
    private lateinit var authService: AuthService

    private lateinit var recHotel: RecyclerView
    private lateinit var recHotelModel: ArrayList<recHotelmodel>
    private lateinit var recHotelAdapter: recHotelAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.fragment_hotel, container, false)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        recHotel = view.findViewById(R.id.recHotel)
        val interNetConnection = InterNetConnection()
        if (interNetConnection.checkForInternet(requireContext()))
        {
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
                        val hotelManager = GridLayoutManager(context, 2)
                        recHotel.layoutManager = hotelManager
                        recHotel.adapter = recHotelAdapter
                        recHotel.layoutManager = hotelManager
                    }
                }
            }
        }else
        {
            Toast.makeText(context, "Please Connect To Internet!!", Toast.LENGTH_LONG)
                .show()
        }
    }
}