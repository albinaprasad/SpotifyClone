package com.albin.spotify.Views

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class applicationClass:Application() {

    override fun onCreate() {
        super.onCreate()


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
       {
           val channel= NotificationChannel("id1","channel1", NotificationManager.IMPORTANCE_HIGH)
           val manager: NotificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

           manager.createNotificationChannel(channel)
       }
    }
}