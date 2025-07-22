package com.albin.spotify.Views

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.albin.spotify.R

class musicService: Service() {

    var mediaPlayer: MediaPlayer? = null


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when(intent?.action){

            Actions.START.toString()->startMusic(
                intent.getStringExtra("SONG_PATH"),
                intent.getStringExtra("SONG_TITLE") ?: "Unknown")


            Actions.STOP.toString()->stopSelf()
        }

        return START_STICKY
    }

    enum class  Actions {
        START,STOP
    }

    fun startMusic(SongPath:String?,SongTitle: String) {


        val stopIntent = Intent(this, musicService::class.java).apply {
            action = Actions.STOP.toString()
        }

            val stopPendingIntent = PendingIntent.getService(
            this,
            Actions.STOP.ordinal, // Unique number for the STOP action
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, "curent_channel")
            .setSmallIcon(R.drawable.musical_icon)
            .setContentTitle("Now Playing")
            .setContentText(SongTitle)
            .addAction(R.drawable.pause, "Stop", stopPendingIntent)
            .setOngoing(true)
            .build()

        startForeground(1, notification)

    }


    override fun onCreate() {
            super.onCreate()

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                val channel= NotificationChannel("curent_channel","name1", NotificationManager.IMPORTANCE_HIGH)
                val notificationmanager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationmanager.createNotificationChannel(channel)
            }
        }




}