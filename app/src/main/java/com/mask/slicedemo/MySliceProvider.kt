package com.mask.slicedemo

import android.R
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.SliceProvider
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.SliceAction
import java.util.concurrent.TimeUnit

/**
 * Created by Manokar on 5/14/18.
 */
class MySliceProvider : SliceProvider() {

    companion object {
        var sReqCode = 0
    }

    override fun onBindSlice(sliceUri: Uri?): Slice {
        return when(sliceUri) {
            Constants.CONTENT_URI -> createTemperatureSlice(sliceUri)
            Constants.WIFI_URI -> createWifiSlice(sliceUri)
            else -> ListBuilder(context, sliceUri!!, TimeUnit.SECONDS.toMillis(10))
                    .addRow{ it.setTitle("URI not found.")}
                    .build()
        }
    }

    private fun createWifiSlice(sliceUri: Uri): Slice {
        val wifiManager = context.getSystemService(WifiManager::class.java)
        val isWifiEnabled = wifiManager.isWifiEnabled
        val wifiState = wifiManager.wifiState
        return ListBuilder(context, sliceUri, TimeUnit.SECONDS.toMillis(10)).apply {

            val primaryAction = SliceAction(getWifiSettingPendingIntent(),
                    IconCompat.createWithResource(context, com.mask.slicedemo.R.drawable.ic_wifi), "Wi-Fi Settings")

            val toggleAction = SliceAction(getWifiTogglePendingIntent(isWifiEnabled), "Toggle Wifi", isWifiEnabled)

            ListBuilder.RowBuilder(this)
                    .apply {
                        setTitle("Wi-Fi")
                        setPrimaryAction(primaryAction)
                        when (wifiState){
                            WifiManager.WIFI_STATE_DISABLING -> {
                                setSubtitle("Disabling Wifi...")
                                setContentDescription("Disabling Wifi...")
                            }
                            WifiManager.WIFI_STATE_ENABLING -> {
                                setSubtitle("Enabling Wifi...")
                                setContentDescription("Enabling Wifi...")
                            }
                            WifiManager.WIFI_STATE_UNKNOWN -> {
                                setSubtitle("State Unknown...")
                                setContentDescription("State Unknown...")
                            }
                            else -> {
                                setSubtitle("State Unknown...")
                                addEndItem(toggleAction)
                            }
                        }
                        addRow(this)
                    }
        }.build()
    }

    private fun isWifiDisabling(wifiState:Int) : Boolean{
        return wifiState == WifiManager.WIFI_STATE_DISABLING
    }

    private fun isWifiEnabling(wifiState:Int) : Boolean{
        return wifiState == WifiManager.WIFI_STATE_ENABLING
    }

    private fun getWifiTogglePendingIntent(isEnabled : Boolean) : PendingIntent{
        val intent = Intent(WifiReceiver.CHANGE_STATE_WIFI)
        intent.setClass(context, WifiReceiver::class.java)
        intent.putExtra(WifiReceiver.EXTRA_WIFI_STATE, !isEnabled)
        return PendingIntent.getBroadcast(context, 3, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getWifiSettingPendingIntent() : PendingIntent{
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        return PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createTemperatureSlice(sliceUri: Uri): Slice {
        return ListBuilder(context, sliceUri, TimeUnit.SECONDS.toMillis(10)).apply {

            ListBuilder.RowBuilder(this).apply {
                setTitle(Constants.getTemperatureString(context))
                SliceAction(getChangeTempIntent(Constants.sTemperature+1),
                        IconCompat.createWithResource(context, com.mask.slicedemo.R.drawable.ic_arrow_increase), "Increase Temprature")
                        .apply {
                            addEndItem(this)
                        }

                SliceAction(getChangeTempIntent(Constants.sTemperature-1),
                        IconCompat.createWithResource(context, com.mask.slicedemo.R.drawable.ic_arrow_decrease), "Decrease Temprature")
                        .apply {
                            addEndItem(this)
                        }

                SliceAction(getPrimaryIntent(), IconCompat.createWithResource(context, R.drawable.ic_lock_idle_alarm), "Temperature Controls")
                        .apply {
                            setPrimaryAction(this)
                        }

                addRow(this)

            }
        }.build()
    }

    private fun getPrimaryIntent() : PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    private fun getChangeTempIntent(i: Int): PendingIntent {
        val intent = Intent(MyBroadcastReceiver.ACTION_CHANGE_TEMP)
        intent.setClass(context, MyBroadcastReceiver::class.java)
        intent.putExtra(MyBroadcastReceiver.EXTRA_TEMP_VAL, i)
        return PendingIntent.getBroadcast(context, sReqCode++, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onCreateSliceProvider(): Boolean {
        return true
    }

}