package international.tourism.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import international.tourism.app.R
import international.tourism.app.models.Place
import java.util.ArrayList

class HomePlaceAdapter(
    private var context: Context,
    private var places: ArrayList<Place>,
    private var clickListener: OnItemClickListener? = null
) : RecyclerView.Adapter<HomePlaceAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.card_view_design, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bind(places[position])
    }

    override fun getItemCount(): Int
    {
        return 4
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private var image: ImageView = itemView.findViewById(R.id.cardImage)
        private var name: TextView = itemView.findViewById(R.id.txtName)
        private var city: TextView = itemView.findViewById(R.id.txtCity)

        fun bind(place: Place)
        {
            Glide.with(context).load(place.PlaceImage).into(image)

            name.text = place.PlaceName
            city.text = place.CityName

            itemView.setOnClickListener { clickListener?.onClick(place) }
        }
    }

    interface OnItemClickListener
    {
        fun onClick(place: Place)
    }
}