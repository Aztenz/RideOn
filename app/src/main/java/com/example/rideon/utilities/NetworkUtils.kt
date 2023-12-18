package com.example.rideon.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.lang.ref.WeakReference

class NetworkUtils private constructor(private val context: Context) {

    private val applicationContext = context.applicationContext
    private val connectivityManager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean>
        get() = _isConnected

    private var lastConnectedState: Boolean? = null

    init {
        // Initial network status check
        updateNetworkStatus()

        // Register network callback for continuous monitoring
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                updateNetworkStatus()
            }

            override fun onLost(network: Network) {
                updateNetworkStatus()
            }
        })
    }

    private fun updateNetworkStatus() {
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        val isConnected = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        if (isConnected != lastConnectedState) {
            _isConnected.postValue(isConnected)
            lastConnectedState = isConnected
        }
    }


    companion object {
        @Volatile
        private var instanceRef: WeakReference<NetworkUtils>? = null

        fun getInstance(context: Context): NetworkUtils =
            instanceRef?.get() ?: synchronized(this) {
                val newInstance = NetworkUtils(context)
                instanceRef = WeakReference(newInstance)
                newInstance
            }
    }

}
