package com.albin.spotify.Views

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.albin.spotify.MainActivity
import com.albin.spotify.Music
import com.albin.spotify.R
import com.albin.spotify.databinding.ActivityPlayerBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class player : AppCompatActivity() {

    val PlayermusicList = ArrayList<Music>()
    lateinit var playerBinding: ActivityPlayerBinding
     var mediaPlayer: MediaPlayer ?= null

    var isPlaying: Boolean=false

    var position:Int=0
    var startY:Float=0f




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        playerBinding= ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(playerBinding.root)


        PlayermusicList.addAll(MainActivity.musicList)
        position=intent.getIntExtra("index",0)

//set the image for music
        setLayout()
//setup mediaPlayer

setMediaPlayer()

//play and pause
        playerBinding.Pausebtn.setOnClickListener {

            if (isPlaying)
            {
                playerBinding.Pausebtn.setIconResource(R.drawable.playandpause)
                mediaPlayer!!.pause()
                isPlaying=false
                playerBinding.Pausebtn.setIconResource(R.drawable.playandpause)
            }
            else
            {
                playerBinding.Pausebtn.setIconResource(R.drawable.pause)
                mediaPlayer!!.start()
                isPlaying=true

            }

        }

        //nextButton
        playerBinding.Fwrdbtn.setOnClickListener {

            if (position == PlayermusicList.size-1 )
            {
                position=0
            }
            else
            {
                position++
            }

            setMediaPlayer()
            setLayout()
        }

        //previous button
    playerBinding.backbtn.setOnClickListener {
    if (position == 0 )
    {
        position=PlayermusicList.size-1
    }
    else
    {
        position--
    }

    setMediaPlayer()
    setLayout()
}


        //back navigation
        playerBinding.Homebtn.setOnClickListener {

            Log.d("ButtonDebug", "Button clicked!")
            finish()
        }

        //swipe from top to down to go to the mainActivity
        swipeToGoBack()




    }

    private fun sendIntent(action: musicService.Actions, songPath: String?, songTitle: String?) {

        val intent=Intent(this, musicService::class.java).apply {
            this.action=action.toString()
            songPath?.let { putExtra("SONG_PATH", it) }
            songTitle?.let { putExtra("SONG_TITLE", it) }
        }
        startService(intent)
    }

    private fun setMediaPlayer() {


        mediaPlayer?.let {
            it.stop()
            it.reset()
            it.release()
        }
        mediaPlayer = null

        mediaPlayer= MediaPlayer()
        mediaPlayer!!.reset()
        mediaPlayer!!.setDataSource(PlayermusicList[position].path)
        mediaPlayer!!.prepare()
        mediaPlayer!!.start()

        playerBinding.Pausebtn.setIconResource(R.drawable.pause)
        isPlaying=true

        showNotification()


//////////////////////////////////////////////////////////////////////////
//        val songPath = PlayermusicList[position].path
//        val songTitle = PlayermusicList[position].title.toString()
//        sendIntent(musicService.Actions.START, songPath, songTitle)
    }

    private fun setLayout() {

        Glide.with(this).load(PlayermusicList[position].imageuri).apply(RequestOptions.placeholderOf(R.drawable.musical_icon))
            .centerCrop().into(playerBinding.shapeableImageView)

        playerBinding.appCompatTextView.text=PlayermusicList[position].title.toString()


    }

    @SuppressLint("ClickableViewAccessibility")
    fun swipeToGoBack() {

        playerBinding.main.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                startY = event.y
                true
            } else if (event.action == MotionEvent.ACTION_MOVE) {
                val dragDistance = event.y - startY
                if (dragDistance > 0) {
                    view.translationY = dragDistance
                }
                true
            } else if (event.action == MotionEvent.ACTION_UP) {
                val dragDistance = event.y - startY
                val threshold = view.height * 0.4f

                if (dragDistance > threshold) {
                    view.animate()
                        .translationY(view.height.toFloat())
                        .setDuration(200)
                        .withEndAction { finish() }
                        .start()
                } else {
                    view.animate()
                        .translationY(0f)
                        .setDuration(200)
                        .start()
                }
                true
            } else {
                false
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // Ensure MediaPlayer is released
        mediaPlayer?.let {
            it.stop()
            it.reset()
            it.release()
        }
        mediaPlayer = null

    }

    private fun showNotification() {
        if (PlayermusicList.isEmpty()) {
            Log.e("Player", "Music list is empty")
            Toast.makeText(this, "No songs available", Toast.LENGTH_SHORT).show()
            return
        }
        val songPath = PlayermusicList[position].path
        val songTitle = PlayermusicList[position].title.toString()
        sendIntent(musicService.Actions.START, songPath, songTitle)
        playerBinding.Pausebtn.setIconResource(R.drawable.pause)
        isPlaying = true
    }

}




