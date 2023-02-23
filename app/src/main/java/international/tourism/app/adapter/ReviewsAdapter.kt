package international.tourism.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import international.tourism.app.R
import international.tourism.app.models.Review

class ReviewsAdapter(private var context: Context,
                     private var reviews: ArrayList<Review>,
                     private var clickListener: OnItemClickListener? = null
) : RecyclerView.Adapter<ReviewsAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.about_view_design, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bind(reviews[position])
    }

    override fun getItemCount(): Int
    {
        return reviews.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private var lblUserName: TextView = itemView.findViewById(R.id.lblUserName)
        private var userRating: RatingBar = itemView.findViewById(R.id.userRating)
        private var lblReviews: TextView = itemView.findViewById(R.id.lblReviews)

        fun bind(reviews: Review)
        {
            lblUserName.text = reviews.userName
            userRating.numStars = reviews.Stars
            lblReviews.text = reviews.Reviews

            itemView.setOnClickListener { clickListener?.onClick(reviews)}
        }
    }

    interface OnItemClickListener
    {
        fun onClick(reviews: Review)
    }
}