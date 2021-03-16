/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.scottlarson.mooveeapp.movielist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scottlarson.mooveeapp.databinding.LayoutItemTwoRowBinding
import com.scottlarson.mooveeapp.models.Movie

/**
 * A [RecyclerView.ViewHolder] used to describe the [Movie] item view and metadata about its place
 * within the [RecyclerView].
 *
 * @param binding The [Movie] view binding
 */
class MovieListItemViewHolder(
        private val binding: LayoutItemTwoRowBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun inflate(parent: ViewGroup): MovieListItemViewHolder {
            val binding = LayoutItemTwoRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
            )
            return MovieListItemViewHolder(binding)
        }
    }

    fun bind(item: Movie) {
        binding.line1.text = item.title
        binding.line2.text = item.overview
    }
}
