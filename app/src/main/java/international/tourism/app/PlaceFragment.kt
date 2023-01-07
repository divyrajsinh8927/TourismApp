package international.tourism.app

import international.tourism.app.adapter.recPlaceAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import international.tourism.app.models.Place
import international.tourism.app.models.recPlacemodel
import international.tourism.app.repo.AuthService
import kotlinx.coroutines.*
import java.net.HttpURLConnection


class PlaceFragment : Fragment()
{
    private lateinit var authService: AuthService

    private lateinit var recPlace: RecyclerView
    private lateinit var recPlaceModel: ArrayList<recPlacemodel>
    private lateinit var recPlaceAdapter: recPlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        recPlace = view.findViewById(R.id.recPlace)
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


                    for (key in data)
                    {
                        if (key.PlaceIsDelete == 0)
                        {
                            GlobalScope.launch(Dispatchers.Main) {
                                recPlaceModel.add(
                                    recPlacemodel(
                                        "http://192.168.43.23/ATourism/images/".plus(key.PlaceImage),
                                        key.PlaceName,
                                        key.CityName
                                    )
                                )
                                recPlaceAdapter =
                                    context?.let { recPlaceAdapter(it, recPlaceModel) }!!
                                val layoutManager = GridLayoutManager(context, 2)
                                recPlace.layoutManager = layoutManager
                                recPlace.adapter = recPlaceAdapter
                            }
                        }
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