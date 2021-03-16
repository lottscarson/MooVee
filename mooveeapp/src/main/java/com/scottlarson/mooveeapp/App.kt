/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.scottlarson.mooveeapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class App : Application(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override fun onCreate() {
        super.onCreate()
    }

    @SuppressLint("ServiceCast")
    fun hasNetwork(context: Context): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
}
