package com.scottlarson.mooveeapp.moviedetails

import com.scottlarson.mooveeapp.movielist.ReviewListItemViewHolder
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scottlarson.mooveeapp.models.Review

/**
 * A [RecyclerView.Adapter] used to feed [Review] data to the list view and handle creation and
 * replacement of views for the [Movie] data items.
 *
 * @property items List of [Review] data items to display.
 * @property itemSelectedListener Callback which listens for list item select events.
 */
class ReviewListItemAdapter(
    private val items: List<Review>,
    private val itemSelectedListener: (Review) -> Unit
) : RecyclerView.Adapter<ReviewListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewListItemViewHolder {
        return ReviewListItemViewHolder.inflate(
            parent
        )
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ReviewListItemViewHolder, position: Int) {
        val item = items[position]

        holder.bind(item)
        holder.itemView.setOnClickListener {
            itemSelectedListener(item)
        }
    }
}
