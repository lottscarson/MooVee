/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.scottlarson.mooveeapp.movielist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scottlarson.mooveeapp.models.Movie

/**
 * A [RecyclerView.Adapter] used to feed [Movie] data to the list view and handle creation and
 * replacement of views for the [Movie] data items.
 *
 * @property items List of [Movie] data items to display.
 * @property itemSelectedListener Callback which listens for list item select events.
 */
class MovieListItemAdapter(
    private val items: List<Movie>,
    private val itemSelectedListener: (Movie) -> Unit
) : RecyclerView.Adapter<MovieListItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListItemViewHolder {
        return MovieListItemViewHolder.inflate(
            parent
        )
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: MovieListItemViewHolder, position: Int) {
        val item = items[position]

        holder.bind(item)
        holder.itemView.setOnClickListener {
            itemSelectedListener(item)
        }
    }
}
