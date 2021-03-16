/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.scottlarson.mooveeapp.movielist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scottlarson.mooveeapp.databinding.LayoutItemRatingBinding
import com.scottlarson.mooveeapp.models.Review

/**
 * A [RecyclerView.ViewHolder] used to describe the [Review] item view and metadata about its place
 * within the [RecyclerView].
 *
 * @param binding The [Review] view binding
 */
class ReviewListItemViewHolder(
    private val binding: LayoutItemRatingBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun inflate(parent: ViewGroup): ReviewListItemViewHolder {
            val binding = LayoutItemRatingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ReviewListItemViewHolder(binding)
        }
    }

    fun bind(item: Review) {
        binding.labelAuthor.text = "Author: " + item.author
        if (item.authorDetails != null) {
            binding.labelRating.text = "Rating: " + item.authorDetails.rating
        }
        binding.line2.text = item.content
    }
}
