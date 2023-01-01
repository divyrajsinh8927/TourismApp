package Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import international.tourism.app.R
import international.tourism.app.models.recPlacemodel
import java.util.ArrayList

  class recPlaceAdapter(
    private var mycontext: Context,
    private var model: ArrayList<recPlacemodel>
) : RecyclerView.Adapter<recPlaceAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val myview =
            LayoutInflater.from(mycontext).inflate(R.layout.card_view_design, null, true)
        return ViewHolder(myview)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model2 = model[position]
        holder.image.setImageResource(model2.image)
        holder.name.text = model2.name
        holder.state.text = model2.state
    }

    override fun getItemCount(): Int {
        return model.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var name: TextView
        var state: TextView

        init {
            image = itemView.findViewById(R.id.cardimage)
            name = itemView.findViewById(R.id.txtName)
            state = itemView.findViewById(R.id.txtState)
        }
    }
}