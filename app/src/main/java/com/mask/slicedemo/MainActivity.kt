package com.mask.slicedemo

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        val ACTION_TEMP_CHANGE = "com.mask.android.TEMP_CHANGE"
    }

    lateinit var  mTvTemperature : TextView

    private val intentFilter = IntentFilter(ACTION_TEMP_CHANGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTvTemperature = findViewById(R.id.tvTemperature)

        imgArrowDown.setOnClickListener {
            updateTemperature(Constants.sTemperature-1)
        }

        imgArrowUp.setOnClickListener {
            updateTemperature(Constants.sTemperature+1)
        }

        lifecycle.addObserver(object : LifecycleObserver{
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun updateTemperature(){
                mTvTemperature.text = Constants.getTemperatureString(this@MainActivity)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun registerBroadcastReceiver(){
                LocalBroadcastManager.getInstance(this@MainActivity).registerReceiver(receiver, intentFilter)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun unregisterBroadcastReceiver(){
                LocalBroadcastManager.getInstance(this@MainActivity).unregisterReceiver(receiver)
            }
        })

    }

    private fun updateTemperature(i : Int){
        val intent = Intent(MyBroadcastReceiver.ACTION_CHANGE_TEMP)
        intent.setClass(this, MyBroadcastReceiver::class.java)
        intent.putExtra(MyBroadcastReceiver.EXTRA_TEMP_VAL, i)
        sendBroadcast(intent)
    }

    private val receiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            mTvTemperature.text = Constants.getTemperatureString(this@MainActivity)
        }
    }

}