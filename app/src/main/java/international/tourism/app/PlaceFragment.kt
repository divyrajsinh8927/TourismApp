package international.tourism.app

import international.tourism.app.Adapter.recPlaceAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import international.tourism.app.models.recPlacemodel

class PlaceFragment : Fragment() {

    private lateinit var recPlace: RecyclerView
    private lateinit var recPlacemodel: ArrayList<recPlacemodel>
    private lateinit var recPlaceAdapter: recPlaceAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Place Recycle View */
        recPlace = view.findViewById(R.id.recPlace)
        recPlacemodel = ArrayList()
        recPlacemodel.add(recPlacemodel(R.drawable.ic_place,"PlaceName","State"))
        recPlacemodel.add(recPlacemodel(R.drawable.ic_place,"PlaceName","State"))
        recPlacemodel.add(recPlacemodel(R.drawable.ic_place,"PlaceName","State"))
        recPlacemodel.add(recPlacemodel(R.drawable.ic_place,"PlaceName","State"))
        recPlacemodel.add(recPlacemodel(R.drawable.ic_place,"PlaceName","State"))
        recPlacemodel.add(recPlacemodel(R.drawable.ic_place,"PlaceName","State"))
        recPlacemodel.add(recPlacemodel(R.drawable.ic_place,"PlaceName","State"))
        recPlaceAdapter = context?.let { recPlaceAdapter(it, recPlacemodel) }!!
        val LayoutManager = GridLayoutManager(context,2)
        recPlace.layoutManager = LayoutManager
        recPlace.adapter = recPlaceAdapter
        recPlace.adapter = recPlaceAdapter
    }

}