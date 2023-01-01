package international.tourism.app

import Adapter.recHotelAdapter
import Adapter.recPlaceAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import international.tourism.app.models.recHotelmodel
import international.tourism.app.models.recPlacemodel

class HotelFragment : Fragment()
{
    private lateinit var recHotel: RecyclerView
    private lateinit var recHotelmodel: ArrayList<recHotelmodel>
    private lateinit var recHotelAdapter: recHotelAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.fragment_hotel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recHotel = view.findViewById(R.id.recHotel)
        recHotelmodel = ArrayList()
        recHotelmodel.add(recHotelmodel(R.drawable.ic_hotel,"HotelName","State"))
        recHotelmodel.add(recHotelmodel(R.drawable.ic_hotel,"HotelName","State"))
        recHotelmodel.add(recHotelmodel(R.drawable.ic_hotel,"HotelName","State"))
        recHotelmodel.add(recHotelmodel(R.drawable.ic_hotel,"HotelName","State"))
        recHotelmodel.add(recHotelmodel(R.drawable.ic_hotel,"HotelName","State"))
        recHotelmodel.add(recHotelmodel(R.drawable.ic_hotel,"HotelName","State"))
        recHotelmodel.add(recHotelmodel(R.drawable.ic_hotel,"HotelName","State"))
        recHotelAdapter =   recHotelAdapter(requireContext(), recHotelmodel)
        val LayoutManager = GridLayoutManager(context,2)
        recHotel.layoutManager = LayoutManager
        recHotel.adapter = recHotelAdapter
        recHotel.adapter = recHotelAdapter
    }
}