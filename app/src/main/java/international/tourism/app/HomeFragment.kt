package international.tourism.app

import international.tourism.app.Adapter.recHotelAdapter
import international.tourism.app.Adapter.recPlaceAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import international.tourism.app.models.recHotelmodel
import international.tourism.app.models.recPlacemodel

class HomeFragment : Fragment()
{
    private lateinit var recHomePlace: RecyclerView
    private lateinit var recHomeHotel: RecyclerView
    private lateinit var recPlacemodel: ArrayList<recPlacemodel>
    private lateinit var recHotelmodel: ArrayList<recHotelmodel>
    private lateinit var manager: LinearLayoutManager
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Place Recycle View */
        recHomePlace = view.findViewById(R.id.recHomePlace)
        recPlacemodel = ArrayList()
        recPlacemodel.add(recPlacemodel(R.drawable.ic_place,"PlaceName","State"))
        recPlacemodel.add(recPlacemodel(R.drawable.ic_place,"PlaceName","State"))
        recPlacemodel.add(recPlacemodel(R.drawable.ic_place,"PlaceName","State"))
        recPlacemodel.add(recPlacemodel(R.drawable.ic_place,"PlaceName","State"))
        recPlacemodel.add(recPlacemodel(R.drawable.ic_place,"PlaceName","State"))
        recPlacemodel.add(recPlacemodel(R.drawable.ic_place,"PlaceName","State"))
        recPlacemodel.add(recPlacemodel(R.drawable.ic_place,"PlaceName","State"))
        recPlaceAdapter = context?.let { recPlaceAdapter(it, recPlacemodel) }!!
        manager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recHomePlace.adapter = recPlaceAdapter
        recHomePlace.layoutManager = manager

        /* Hotel Recycle View */
        recHomeHotel = view.findViewById(R.id.recHomeHotel)
        recHotelmodel = ArrayList()
        recHotelmodel.add(recHotelmodel(R.drawable.ic_hotel,"HotelName","State"))
        recHotelmodel.add(recHotelmodel(R.drawable.ic_hotel,"HotelName","State"))
        recHotelmodel.add(recHotelmodel(R.drawable.ic_hotel,"HotelName","State"))
        recHotelmodel.add(recHotelmodel(R.drawable.ic_hotel,"HotelName","State"))
        recHotelmodel.add(recHotelmodel(R.drawable.ic_hotel,"HotelName","State"))
        recHotelmodel.add(recHotelmodel(R.drawable.ic_hotel,"HotelName","State"))
        recHotelmodel.add(recHotelmodel(R.drawable.ic_hotel,"HotelName","State"))
        recHotelAdapter =   recHotelAdapter(requireContext(), recHotelmodel)
        manager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recHomeHotel.adapter = recHotelAdapter
        recHomeHotel.layoutManager = manager

    }
}