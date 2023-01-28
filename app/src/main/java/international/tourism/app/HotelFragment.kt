package international.tourism.app

import android.content.Intent
import international.tourism.app.adapter.RecHotelAdapter
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
import international.tourism.app.models.ImagesUrl
import international.tourism.app.repo.HotelService
import kotlinx.coroutines.*
import java.net.HttpURLConnection

class HotelFragment : Fragment()
{
    private lateinit var hotelService: HotelService
    private lateinit var imagesUrl: ImagesUrl

    private lateinit var recHotel: RecyclerView
    private lateinit var hotelList: ArrayList<Hotel>
    private lateinit var recHotelAdapter: RecHotelAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.fragment_hotel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        recHotel = view.findViewById(R.id.recHotel)
        imagesUrl = ImagesUrl()
        val interNetConnection = InterNetConnection()
        if (!interNetConnection.checkForInternet(requireContext()))
        {
            Toast.makeText(context, "Please Connect To Internet!!", Toast.LENGTH_LONG)
                .show()
            return
        }
        configureData()
        recHotel.startLayoutAnimation()

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun configureData()
    {
        CoroutineScope(Dispatchers.IO).launch {
            hotelService = HotelService()
            val hotelResponse = hotelService.getAllHotel()
            if (hotelResponse.code == HttpURLConnection.HTTP_NOT_FOUND)
            {
                Toast.makeText(context, "No Data Found!!", Toast.LENGTH_LONG)
                    .show()
                return@launch
            }
            hotelList = ArrayList()
            val hotelData = Gson().fromJson(hotelResponse.message, Array<Hotel>::class.java)
            GlobalScope.launch(Dispatchers.Main) {

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
                    recHotelAdapter = RecHotelAdapter(
                        requireContext(),
                        hotelList,
                        object : RecHotelAdapter.OnItemClickListener
                        {
                            override fun onClick(hotel: Hotel)
                            {
                                val intent =
                                    Intent(requireContext(), HotelActivity::class.java)
                                intent.putExtra("hotelId", hotel.Id)
                                startActivity(intent)
                            }
                        })
                    recHotel.layoutManager = GridLayoutManager(requireContext(), 1)
                    recHotel.adapter =      recHotelAdapter
                }
            }
        }
    }
}