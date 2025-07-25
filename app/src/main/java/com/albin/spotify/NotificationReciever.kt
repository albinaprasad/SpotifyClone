package com.albin.spotify

import android.app.Service.STOP_FOREGROUND_REMOVE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.widget.Toast
import com.albin.spotify.Views.musicService
import com.albin.spotify.Views.musicService.Companion.PREVIOUS
import com.albin.spotify.Views.player
import com.albin.spotify.Views.player.Companion.PlayermusicList
import com.albin.spotify.Views.player.Companion.isPlaying
import com.albin.spotify.Views.player.Companion.musicservice
import com.albin.spotify.Views.player.Companion.playerBinding
import com.albin.spotify.Views.player.Companion.position
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlin.system.exitProcess

class NotificationReciever: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        when (intent?.action) {

            musicService.PLAY -> playAndPause()
            musicService.NEXT -> playNext(context)
            musicService.PREVIOUS -> PlayPrevious(context)
            musicService.STOP -> stopAPP()
        }
    }

    fun setupMusicService()
    {

        player.musicservice!!. mediaPlayer?.let {
            it.stop()
            it.reset()
            it.release()
        }
        player.musicservice!!. mediaPlayer = null

        player.musicservice!!.mediaPlayer= MediaPlayer()
        player.musicservice!!. mediaPlayer!!.reset()
        player.musicservice!!.mediaPlayer!!.setDataSource(PlayermusicList[position].path)
        player.musicservice!!.mediaPlayer!!.prepare()
        player.musicservice!!.mediaPlayer!!.start()

        player.musicservice!!.showNotification()
        isPlaying=true

    }



    private fun playNext(context:Context?) {

        if (player.position ==player.PlayermusicList.size-1 )
        {
            player.position=0
        }
        else
        {
            player.position++
        }

        setupMusicService()

        val updateIntent = Intent(context, player::class.java).apply {
            action = "Update_the_UI"
            putExtra("position", position)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        context!!.startActivity(updateIntent)
    }

    fun PlayPrevious(context: Context?)
    {
        if (position == 0 )
        {
            position=PlayermusicList.size-1

        }
        else
        {
            position--
        }

        setupMusicService()

        val prevUiIntent= Intent(context,player::class.java).apply {

            action = "previous_ui_update"
            putExtra("previous", position)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        context!!.startActivity(prevUiIntent)
    }


    private fun playAndPause() {

       if (player.isPlaying)
       {
           player.musicservice!!.mediaPlayer!!.pause()
           player.musicservice!!.showNotification()
            player.isPlaying=false
          playerBinding.Pausebtn.setIconResource(R.drawable.playandpause)


       }else {
          player.musicservice!!.mediaPlayer!!.start()
           player.musicservice!!.showNotification()
           player.isPlaying = true
           playerBinding.Pausebtn.setIconResource(R.drawable.pause)
       }
    }

    private fun stopAPP() {
       player.musicservice!!.stopForeground(STOP_FOREGROUND_REMOVE)
        player.musicservice=null
        exitProcess(1)
    }


}