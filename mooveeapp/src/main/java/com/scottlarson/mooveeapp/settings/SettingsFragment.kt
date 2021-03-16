/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.scottlarson.mooveeapp.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.scottlarson.mooveeapp.App
import com.scottlarson.mooveeapp.R
import com.scottlarson.mooveeapp.createLoadingAlertDialog
import com.scottlarson.mooveeapp.databinding.FragmentSettingsBinding
import com.scottlarson.mooveeapp.showAlertDialog
import com.scottlarson.mooveeapp.util.ObjectDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * This [SettingsFragment] presents a settings screen so that the user can navigate to different settings and screens.
 *
 * - Links From:
 *  - [MovieListItemFragment]: A user chooses the "Settings" option from the vault screen toolbar
 */
class SettingsFragment : Fragment(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    /** View binding to the views defined in the layout */
    private val bindingDelegate = ObjectDelegate<FragmentSettingsBinding>()
    private val binding by bindingDelegate

    /** Navigation controller used to manage app navigation. */
    private lateinit var navController: NavController

    /** An [AlertDialog] used to indicate that an operation is occurring. */
    private var loading: AlertDialog? = null

    /** The [Application] that holds references to the APIs this fragment needs */
    private lateinit var app: App

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingDelegate.attach(FragmentSettingsBinding.inflate(inflater, container, false))
        app = requireActivity().application as App
        binding.toolbar.root.title = getString(R.string.settings)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

//        binding.secretCodeButton.setOnClickListener {
//            navController.navigate(SettingsFragmentDirections.actionSettingsFragmentToSecretCodeFragment())
//        }

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        loading?.dismiss()
        coroutineContext.cancelChildren()
        coroutineContext.cancel()
        bindingDelegate.detach()
        super.onDestroy()
    }

    /**
     * Sets main menu buttons and toolbar items to enabled/disabled.
     *
     * @param isEnabled If true, buttons and toolbar items will be enabled.
     */
    private fun setItemsEnabled(isEnabled: Boolean) {
//        binding.secretCodeButton.isEnabled = isEnabled
//        binding.passwordGeneratorButton.isEnabled = isEnabled
//        binding.lockVaultsButton.isEnabled = isEnabled
//        binding.deregisterButton.isEnabled = isEnabled
    }

    /** Displays the loading [AlertDialog] indicating that an operation is occurring. */
    private fun showLoading(@StringRes textResId: Int) {
        loading = createLoadingAlertDialog(textResId)
        loading?.show()
        setItemsEnabled(false)
    }

    /** Dismisses the loading [AlertDialog] indicating that an operation has finished. */
    private fun hideLoading() {
        loading?.dismiss()
        if (bindingDelegate.isAttached()) {
            setItemsEnabled(true)
        }
    }
}
