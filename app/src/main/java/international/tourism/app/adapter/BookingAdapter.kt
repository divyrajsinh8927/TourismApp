package international.tourism.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import international.tourism.app.R
import international.tourism.app.models.Booking

class BookingAdapter(private var context: Context,
                     private var bookings: ArrayList<Booking>,
                     private var clickListener: OnItemClickListener? = null
) : RecyclerView.Adapter<BookingAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.bookig_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bind(bookings[position])
    }

    override fun getItemCount(): Int
    {
        return bookings.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private var lblHotelName: TextView = itemView.findViewById(R.id.lblHotelName)
        private var lblTotalPrice: TextView = itemView.findViewById(R.id.lblTotalPrice)
        private var lblStatus: TextView = itemView.findViewById(R.id.lblStatus)


        fun bind(bookings: Booking)
        {
            lblHotelName.text = bookings.HotelName
            lblTotalPrice.text = bookings.TotalPrice.toString()
            lblStatus.text = bookings.Status

            itemView.setOnClickListener { clickListener?.onClick(bookings) }
        }
    }

    interface OnItemClickListener
    {
        fun onClick(bookings: Booking)
    }
}