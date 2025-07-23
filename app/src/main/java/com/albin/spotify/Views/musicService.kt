package com.albin.spotify.Views

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK

import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.session.MediaSession
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat

import android.util.Log

import androidx.core.app.NotificationCompat
import androidx.palette.graphics.Palette
import com.albin.spotify.R
import com.albin.spotify.Views.player

class musicService: Service() {



    var mediaPlayer: MediaPlayer? = null
    lateinit  var mediasession: MediaSessionCompat

    val mybinder=Mybinder()

    override fun onBind(p0: Intent?): IBinder? {
        mediasession=MediaSessionCompat(this, "MusicServiceTag")
        return mybinder
    }

    inner class Mybinder: Binder()
    {
        fun currentService(): musicService {
            return this@musicService
        }
    }


    @SuppressLint("ForegroundServiceType")
    fun showNotification() {
        try {

            val imageUriString = player.PlayermusicList[player.position].imageuri
            val bitmap = contentResolver.openInputStream(Uri.parse(imageUriString))?.use {
                BitmapFactory.decodeStream(it)

            }

            val noti = NotificationCompat.Builder(baseContext, "id1")
                .setContentTitle(player.PlayermusicList[player.position].title)
                .setContentText(player.PlayermusicList[player.position].singer)
                .setSmallIcon(R.drawable.updates)
                .setLargeIcon(bitmap)
                .setSubText("Now Playing")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.previous, "previous", null)
                .addAction(R.drawable.playandpause, "play", null)
                .addAction(R.drawable.skip_previous, "next", null)
                .addAction(R.drawable.exit, "exit", null)
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediasession.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
                        )
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                startForeground(1, noti.build())
            }
         catch (e: Exception) {
            Log.e("NotificationError", "Error building notification", e)
        }
    }


}