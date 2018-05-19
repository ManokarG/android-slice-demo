package com.mask.slicedemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import androidx.core.net.toUri

/**
 * Created by Manokar on 5/19/18.
 */
class WifiReceiver :BroadcastReceiver(){
    companion object {
        val CHANGE_STATE_WIFI = "com.mask.example.CHANGE_STATE_WIFI"
        val EXTRA_WIFI_STATE = "com.mask.example.EXTRA_WIFI_STATE"
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        p1?.let {
            val wifiManager = p0!!.getSystemService(WifiManager::class.java)
            wifiManager.isWifiEnabled = !wifiManager.isWifiEnabled
        }
    }

}