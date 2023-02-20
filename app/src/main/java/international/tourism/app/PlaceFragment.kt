package international.tourism.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import international.tourism.app.adapter.PlaceAdapter
import international.tourism.app.models.ImagesUrl
import international.tourism.app.models.Place
import international.tourism.app.services.PlaceService
import kotlinx.coroutines.*
import java.net.HttpURLConnection


class PlaceFragment : Fragment()
{
        private lateinit var placeService: PlaceService
        private lateinit var imagesUrl: ImagesUrl
        private lateinit var recPlace: RecyclerView
        private lateinit var placeList: ArrayList<Place>
        private lateinit var recPlaceAdapter: PlaceAdapter
        private lateinit var attachedContext: Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.fragment_place, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        attachedContext = activity ?: return

        imagesUrl = ImagesUrl()
        recPlace = view.findViewById(R.id.recPlace)
        recPlace.startLayoutAnimation()

        val interNetConnection = InterNetConnection()
        if (!interNetConnection.checkForInternet(attachedContext))
        {
            Toast.makeText(context, "Please Connect To Internet!!", Toast.LENGTH_LONG).show()
            return
        }
        populatePlaceData()
    }

    private fun populatePlaceData()
    {
        CoroutineScope(Dispatchers.IO).launch {
            placeService = PlaceService()
            val response = placeService.getAllPlace()

            if (response.code == HttpURLConnection.HTTP_NOT_FOUND)
            {
                Toast.makeText(context, "No Data Found!!", Toast.LENGTH_LONG)
                    .show()
            }

            placeList = ArrayList()
            val places = Gson().fromJson(response.message, Array<Place>::class.java)

            for (place in places)
            {
                if (place.PlaceIsDelete == 1)
                    continue

                withContext(Dispatchers.Main) {
                    placeList.add(
                        Place(
                            Id = place.Id,
                            PlaceImage = imagesUrl.ImageBaseUrl.plus(place.PlaceImage),
                            PlaceName = place.PlaceName,
                            CityName = place.CityName
                        )
                    )
                    recPlaceAdapter = PlaceAdapter(attachedContext, placeList, object: PlaceAdapter.OnItemClickListener
                    {
                        override fun onClick(place: Place)
                        {
                            val intent = Intent(attachedContext, PlaceActivity::class.java)
                            intent.putExtra("placeId", place.Id)
                            startActivity(intent)
                        }
                    })
                    recPlace.layoutManager = GridLayoutManager(attachedContext, 1)
                    recPlace.adapter = recPlaceAdapter
                }
            }
        }
    }
}