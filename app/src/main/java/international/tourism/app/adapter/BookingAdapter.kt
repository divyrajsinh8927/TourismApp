package international.tourism.app.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import international.tourism.app.R
import international.tourism.app.models.Booking

class BookingAdapter(
    private val activity: Activity,
    private val booking: Array<Booking>
):  ArrayAdapter<Booking>(activity, R.layout.bookig_list_item, booking)
{
    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        var view = convertView
        if (view == null)
        {
            view = LayoutInflater.from(activity).inflate(R.layout.bookig_list_item, null)

            val viewHolder = ViewHolder()
            viewHolder.lblHotelName = view.findViewById(R.id.lblHotelName)
            viewHolder.lblTotalPrice= view.findViewById(R.id.lblTotalPrice)
            viewHolder.lblStatus= view.findViewById(R.id.lblStatus)

            view.tag = viewHolder
        }

        val existingViewHolder = view!!.tag as ViewHolder

        existingViewHolder.lblHotelName.text = booking[position].HotelName
        existingViewHolder.lblTotalPrice.text = booking[position].TotalPrice.toString().plus(" Rs")
        existingViewHolder.lblStatus.text = booking[position].Status

        return view
    }

    class ViewHolder
    {
        lateinit var lblHotelName: TextView
        lateinit var lblTotalPrice: TextView
        lateinit var lblStatus: TextView
    }
}