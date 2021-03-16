package com.scottlarson.mooveeapp.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
import com.scottlarson.mooveeapp.databinding.FragmentReviewBinding
import com.scottlarson.mooveeapp.models.Review
import com.scottlarson.mooveeapp.moviedetails.MovieDetailsFragment
import com.scottlarson.mooveeapp.util.ObjectDelegate

/**
 * This [ReviewFragment] presents a screen that shows a movie review
 *
 * - Links From: [MovieDetailsFragment] when the user clicks the a row containing a review
 */
class ReviewFragment : Fragment() {

    /** View binding to the views defined in the layout */
    private val bindingDelegate = ObjectDelegate<FragmentReviewBinding>()
    private val binding by bindingDelegate

    /** Fragment arguments handled by Navigation Library safe args */
    private val args: ReviewFragmentArgs by navArgs()

    /** The [Review] to be shown. */
    private lateinit var review: Review

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingDelegate.attach(FragmentReviewBinding.inflate(inflater, container, false))
        with(binding.toolbar.root) {
            title = "Review"
        }

        review = args.review

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadReview(review)
    }

    override fun onDestroy() {
        bindingDelegate.detach()
        super.onDestroy()
    }

    private fun loadReview(review: Review) {
        binding.labelReviewer.text = review.author
        binding.labelReviewContent.text = review.content
    }
}