package com.mask.slicedemo

import android.content.Context
import androidx.core.net.toUri

/**
 * Created by Manokar on 5/19/18.
 */
class Constants {
    companion object {

        var sTemperature = 10

        val CONTENT_URI = "content://com.example.android.app/temperature".toUri()
        val WIFI_URI = "content://com.example.android.app/wifi".toUri()

        fun getTemperatureString(context: Context) : String{
            return context.getString(R.string.label_temperature, sTemperature)
        }

    }

}