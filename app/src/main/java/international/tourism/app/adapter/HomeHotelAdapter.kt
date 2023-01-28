package international.tourism.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import international.tourism.app.R
import international.tourism.app.models.Hotel

class HomeHotelAdapter(private var context: Context,
                       private var hotels: ArrayList<Hotel>,
                       private var clickListener: OnItemClickListener? = null
) : RecyclerView.Adapter<HomeHotelAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.card_view_design, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(hotels[position])

    }

    override fun getItemCount(): Int {
        return 4
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var image: ImageView = itemView.findViewById(R.id.cardImage)
        private var name: TextView = itemView.findViewById(R.id.txtName)
        private var city: TextView = itemView.findViewById(R.id.txtCity)

        fun bind(hotel: Hotel)
        {
            Glide.with(context).load(hotel.HotelImage).into(image)

            name.text = hotel.HotelName
            city.text = hotel.CityName

            itemView.setOnClickListener { clickListener?.onClick(hotel) }
        }
    }
    interface OnItemClickListener
    {
        fun onClick(hotel: Hotel)
    }
}