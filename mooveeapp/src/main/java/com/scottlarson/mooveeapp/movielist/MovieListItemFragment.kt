/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.scottlarson.mooveeapp.movielist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scottlarson.mooveeapp.App
import com.scottlarson.mooveeapp.R
import com.scottlarson.mooveeapp.service.MovieServiceBuilder
import com.scottlarson.mooveeapp.databinding.FragmentMovieItemsBinding
import com.scottlarson.mooveeapp.models.Movie
import com.scottlarson.mooveeapp.util.ObjectDelegate
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * This [MovieListItemFragment] presents a list of [Movie]s.
 *
 * - Links To:
 *  - [MovieDetailsFragment]: If a user chooses a [Movie] from the list, the [MovieDetailsFragment]
 *   will be presented
 */
class MovieListItemFragment : Fragment(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    /** View binding to the views defined in the layout */
    private val bindingDelegate = ObjectDelegate<FragmentMovieItemsBinding>()
    private val binding by bindingDelegate

    /** Navigation controller used to manage app navigation. */
    private lateinit var navController: NavController

    /** A reference to the [RecyclerView.Adapter] handling [Movie] data. */
    private lateinit var adapter: MovieListItemAdapter

    /** An [AlertDialog] used to indicate that an operation is occurring. */
    private var loading: AlertDialog? = null

    /** The mutable list of [Movie]s that have been loaded from the SDK. */
    private val itemList = mutableListOf<Movie>()

    private lateinit var app: App

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingDelegate.attach(FragmentMovieItemsBinding.inflate(inflater, container, false))

        app = requireActivity().application as App

        with(binding.toolbar.root) {
            title = getString(R.string.items)
            inflateMenu(R.menu.nav_menu_with_settings)
            setOnMenuItemClickListener {
                when (it?.itemId) {
                    R.id.settings -> {
                        navController.navigate(MovieListItemFragmentDirections.actionMovieItemsFragmentToSettingsFragment())
                    }
                }
                true
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerView()
        navController = Navigation.findNavController(view)
    }

    override fun onResume() {
        super.onResume()
        listItems()
    }

    override fun onDestroy() {
        loading?.dismiss()
        coroutineContext.cancelChildren()
        coroutineContext.cancel()
        bindingDelegate.detach()
        super.onDestroy()
    }

    /**
     * List [Movies]s
     */
    private fun listItems() {
        launch {
            try {
                showLoading(R.string.loading_items)
                launch(Dispatchers.Main) {
                    try {
                        val response = MovieServiceBuilder.movieService.getMovies()
                        if (response.isSuccessful && response.body() != null) {
                            val items = response.body()!!.movies
                            itemList.clear()
                            for (item in items) {
                                itemList.add(item)
                            }

                            itemList.sortWith(
                                Comparator { lhs, rhs ->
                                    when {
                                        lhs.releaseDate.before(rhs.releaseDate) -> -1
                                        lhs.releaseDate.after(rhs.releaseDate) -> 1
                                        else -> 0
                                    }
                                }
                            )
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
     * Configures the [RecyclerView] used to display the listed [Movie] items
     */
    private fun configureRecyclerView() {
        adapter = MovieListItemAdapter(itemList) { item ->
            navController.navigate(MovieListItemFragmentDirections.actionMovieItemsFragmentToMovieDetailsFragment(item))
        }
        binding.itemRecyclerView.adapter = adapter
        binding.itemRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    /**
     * Sets buttons and recycler view to enabled/disabled.
     *
     * @param isEnabled If true, the recycler view will be enabled.
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
