/*
 * SPDX-License-Identifier: Apache-2.0
 */
package com.scottlarson.mooveeapp.moviedetails

//import com.scottlarson.mooveeapp.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scottlarson.mooveeapp.App
import com.scottlarson.mooveeapp.R
import com.scottlarson.mooveeapp.databinding.FragmentMovieDetailsBinding
import com.scottlarson.mooveeapp.models.Movie
import com.scottlarson.mooveeapp.models.Review
import com.scottlarson.mooveeapp.service.MovieServiceBuilder
import com.scottlarson.mooveeapp.util.ObjectDelegate
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import kotlin.coroutines.CoroutineContext

/**
 * This [MovieDetailsFragment] presents a screen that shows the details of a given movie
 *
 * - Links To:
 *  - [ReviewFragment]: If a user chooses a [Review] from the list, the [ReviewFragment]
 *   will be presented
 *
 * - Links From: [MovieListFragment] when the user clicks the a row containing a movie
 */
class MovieDetailsFragment : Fragment(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    /** View binding to the views defined in the layout */
    private val bindingDelegate = ObjectDelegate<FragmentMovieDetailsBinding>()
    private val binding by bindingDelegate

    /** Navigation controller used to manage app navigation. */
    private lateinit var navController: NavController

    /** A reference to the [RecyclerView.Adapter] handling [Review] data. */
    private lateinit var adapter: ReviewListItemAdapter

    /** An [AlertDialog] used to indicate that an operation is occurring. */
    private var loading: AlertDialog? = null

    /** The [Application] that holds references to the APIs this fragment needs. */
    private lateinit var app: App

    /** Fragment arguments handled by Navigation Library safe args */
    private val args: MovieDetailsFragmentArgs by navArgs()

    /** The [Movie] to be shown. */
    private lateinit var movie: Movie

    /** The mutable list of [Review]s that have been loaded from the SDK. */
    private val itemList = mutableListOf<Review>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingDelegate.attach(FragmentMovieDetailsBinding.inflate(inflater, container, false))
        with(binding.toolbar.root) {
            title = "Details"
        }
        app = requireActivity().application as App

        movie = args.movie

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerView()
        navController = Navigation.findNavController(view)

        loadMovieDetails()
    }

    override fun onDestroy() {
        loading?.dismiss()
        coroutineContext.cancelChildren()
        coroutineContext.cancel()
        bindingDelegate.detach()
        super.onDestroy()
    }

    private fun loadMovieDetails() {
        launch {
            try {
                showLoading(R.string.loading_details)
                launch(Dispatchers.Main) {
                    try {
                        val response = MovieServiceBuilder.getInstance(app).movieService.getDetails(movie.id)
                        if (response.isSuccessful && response.body() != null) {
                            val item = response.body()!!
                            binding.labelTitle.text = item.title
                            val pattern = "yyyy-MM-dd"
                            val simpleDateFormat = SimpleDateFormat(pattern)
                            val date: String = simpleDateFormat.format(item.releaseDate)
                            binding.labelRelease.text = "Release Date: " + date
                            loadReviews()
                        }
                    } catch (e: Exception){
                        hideLoading()
                    }
                }
            } catch (e: Exception) {
                hideLoading()
            }
        }
    }

    private fun loadReviews() {
        launch {
            try {
//                showLoading(R.string.loading_reviews)
                launch(Dispatchers.Main) {
                    try {
                        val reviewResponse = MovieServiceBuilder.getInstance(app).movieService.getReviews(movie.id)
                        if (reviewResponse.isSuccessful && reviewResponse.body() != null) {
                            itemList.clear()
                            val items = reviewResponse.body()!!.reviews
                            for (item in items) {
                                itemList.add(item)
                            }

                            adapter.notifyDataSetChanged()
                            hideLoading()
                        }
                    } catch (e: Exception){
                        hideLoading()
                    }
                }
            } catch (e: Exception) {
                hideLoading()
            }
        }
    }

    /**
     * Configures the [RecyclerView] used to display the listed [Review] items
     */
    private fun configureRecyclerView() {
        adapter = ReviewListItemAdapter(itemList) { item ->
            navController.navigate(MovieDetailsFragmentDirections.actionMovieDetailsFragmentToReviewFragment(item))
        }
        binding.itemRecyclerView.adapter = adapter
        binding.itemRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    /**
     * Sets toolbar items and edit text field to enabled/disabled.
     *
     * @param isEnabled If true, toolbar items and edit text field will be enabled.
     */
    private fun setItemsEnabled(isEnabled: Boolean) {
        binding.itemRecyclerView.isEnabled = isEnabled
    }

    /** Displays the progress bar spinner indicating that an operation is occurring. */
    private fun showLoading(@StringRes textResId: Int = 0) {
        if (textResId != 0) {
            binding.progressText.text = getString(textResId)
        }
        binding.progressBar.visibility = View.VISIBLE
        binding.progressText.visibility = View.VISIBLE
        binding.itemRecyclerView.visibility = View.GONE
        setItemsEnabled(false)
    }

    /** Hides the progress bar spinner indicating that an operation has finished. */
    private fun hideLoading() {
        if (bindingDelegate.isAttached()) {
            binding.progressBar.visibility = View.GONE
            binding.progressText.visibility = View.GONE
            binding.itemRecyclerView.visibility = View.VISIBLE
            setItemsEnabled(true)
        }
    }
}
