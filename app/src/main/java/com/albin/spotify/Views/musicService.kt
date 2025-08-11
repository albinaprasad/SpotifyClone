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
import android.media.MediaMetadata
import android.media.MediaPlayer
import android.media.session.MediaSession
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager

import android.util.Log
import androidx.annotation.RequiresApi

import androidx.core.app.NotificationCompat
import androidx.palette.graphics.Palette
import com.albin.spotify.NotificationReciever
import com.albin.spotify.R
import com.albin.spotify.Views.player
import com.albin.spotify.Views.player.Companion.musicservice
import com.albin.spotify.Views.player.Companion.playerBinding
import kotlinx.coroutines.Runnable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.invoke


class musicService : Service() {

    companion object {
        val PREVIOUS = "previous"
        val NEXT = "next"
        val STOP = "stop"
        val PLAY = "play"


    }

    var mediaPlayer: MediaPlayer? = null

    var player1 = player()

    lateinit var runnable: Runnable
    lateinit var mediasession: MediaSessionCompat
    lateinit var telephonyManager: TelephonyManager
    private var wasPlayingBeforeCall: Boolean = false
    private var callCallback: CallStateCallback? = null

    val mybinder = Mybinder()

    override fun onBind(p0: Intent?): IBinder? {
        mediasession = MediaSessionCompat(this, "MusicServiceTag")
        return mybinder
    }

    inner class Mybinder : Binder() {
        fun currentService(): musicService {
            return this@musicService
        }
    }

    //telephony manager setup

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate() {
        super.onCreate()
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        callCallback = CallStateCallback(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            telephonyManager.registerTelephonyCallback(
                Executors.newSingleThreadExecutor(),
                callCallback!!
            )
        } else {
            telephonyManager.listen(object : android.telephony.PhoneStateListener() {
                override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                    handleCallState(state)
                }
            }, PhoneStateListener.LISTEN_CALL_STATE)
        }

    }

    @SuppressLint("ForegroundServiceType")
    fun showNotification() {
        try {

            val imageUriString = player.PlayermusicList[player.position].imageuri
            val bitmap = contentResolver.openInputStream(Uri.parse(imageUriString))?.use {
                BitmapFactory.decodeStream(it)

            }


            val Previntent = Intent(this, NotificationReciever::class.java).setAction(PREVIOUS)
            val prevPendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                Previntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val Nextintent = Intent(this, NotificationReciever::class.java).setAction(NEXT)
            val nextPendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                Nextintent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val Exitintent = Intent(this, NotificationReciever::class.java).setAction(STOP)
            val exitPendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                Exitintent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val Playintent = Intent(this, NotificationReciever::class.java).setAction(PLAY)
            val playPendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                Playintent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )


            val contentIntent = Intent(this, player::class.java).apply {
                putExtra("index", player.position)
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

            val contentPendingIntent = PendingIntent.getActivity(
                this,
                0,
                contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val playPauseIcon = if (player.isPlaying) {
                R.drawable.playandpause
            } else {
                R.drawable.pause
            }

            val playPauseText = if (player.isPlaying) {
                "Pause"

            } else {
                "play"
            }

            val noti = NotificationCompat.Builder(baseContext, "id1")
                .setContentTitle(player.PlayermusicList[player.position].title)
                .setContentText(player.PlayermusicList[player.position].singer)
                .setSmallIcon(R.drawable.updates)
                .setLargeIcon(bitmap)
                .setSubText("Now Playing")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.previous, "previous", prevPendingIntent)
                .addAction(playPauseIcon, playPauseText, playPendingIntent)
                .addAction(R.drawable.skip_previous, "next", nextPendingIntent)
                .addAction(R.drawable.exit, "exit", exitPendingIntent)
                .setContentIntent(contentPendingIntent)
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediasession.sessionToken)
                        .setShowActionsInCompactView(0, 1, 2)
                )
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build()

            startForeground(1, noti)
        } catch (e: Exception) {
            Log.e("NotificationError", "Error building notification", e)
        }


    }

    fun seekbarSetup() {

        runnable = Runnable {
            playerBinding.currentTimeTextView.text =
                player1.formatTimeDuration(musicservice!!.mediaPlayer!!.currentPosition.toLong())
            playerBinding.musicSeekBar.progress = musicservice!!.mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)


    }

    @RequiresApi(Build.VERSION_CODES.S)
    class CallStateCallback(val service: musicService) : TelephonyCallback(),
        TelephonyCallback.CallStateListener {
        override fun onCallStateChanged(state: Int) {
            service.handleCallState(state)
        }

    }

    private fun handleCallState(state: kotlin.Int) {

        when (state) {
            TelephonyManager.CALL_STATE_RINGING, TelephonyManager.CALL_STATE_OFFHOOK -> {

                if (player.isPlaying) {
                    wasPlayingBeforeCall = true
                    player.isPlaying = false
                    mediaPlayer?.pause()
                    showNotification()

                }
            }

            TelephonyManager.CALL_STATE_IDLE -> {

                if (wasPlayingBeforeCall) {
                    player.isPlaying = true
                    wasPlayingBeforeCall = false
                    mediaPlayer?.start()
                    showNotification()
                    seekbarSetup()

                }
            }
        }

    }
}


