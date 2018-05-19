package com.mask.slicedemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.math.MathUtils
import androidx.core.net.toUri
import androidx.localbroadcastmanager.content.LocalBroadcastManager

/**
 * Created by Manokar on 5/19/18.
 */
class MyBroadcastReceiver : BroadcastReceiver() {

    companion object {
        val ACTION_CHANGE_TEMP = "com.mask.slicedemo.ACTION_CHANGE_TEMP"
        val EXTRA_TEMP_VAL = "com.mask.slicedemo.EXTRA_TEMP_VAL"
    }

    override fun onReceive(p0: Context?, p1: Intent?) {
        val action = p1?.action
        if(ACTION_CHANGE_TEMP == action && p1.extras != null){
            val newValue = p1.extras.getInt(EXTRA_TEMP_VAL, Constants.sTemperature)
            updateTemperature(p0, newValue)
        }
    }

    private fun updateTemperature(p0: Context?,newValue: Int) {
        val nowValue = MathUtils.clamp(newValue, 10, 30)
        if(nowValue != Constants.sTemperature){
            Constants.sTemperature = nowValue
            p0?.contentResolver?.notifyChange(Constants.CONTENT_URI, null)
            LocalBroadcastManager.getInstance(p0).sendBroadcast(Intent(MainActivity.ACTION_TEMP_CHANGE))
        }
    }

}