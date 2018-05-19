package com.mask.slicedemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiInfo
import androidx.core.content.ContextCompat.getSystemService
import android.net.wifi.WifiManager
import android.net.NetworkInfo



/**
 * Created by Manokar on 5/19/18.
 */
class WifiStateReceiver :BroadcastReceiver(){
    override fun onReceive(p0: Context?, p1: Intent?) {
        p1?.let {
            if(p1.action == "android.net.wifi.WIFI_STATE_CHANGED"){
                p0?.contentResolver?.notifyChange(Constants.WIFI_URI, null)
            }
        }
    }
}