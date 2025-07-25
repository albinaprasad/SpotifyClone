package com.albin.spotify.Views

import android.annotation.SuppressLint
import android.app.ComponentCaller
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.IBinder
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
import com.albin.spotify.NotificationReciever
import com.albin.spotify.R
import com.albin.spotify.databinding.ActivityPlayerBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class player : AppCompatActivity(), ServiceConnection {


    companion object{
        val PlayermusicList = ArrayList<Music>()
        var position:Int=0
        var musicservice : musicService?=null
        var isPlaying: Boolean=false
        lateinit var playerBinding: ActivityPlayerBinding

    }




    var startY:Float=0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        playerBinding= ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(playerBinding.root)

        // starting the music service
        val serviceintent= Intent(this, musicService::class.java)
        bindService(serviceintent,this,BIND_AUTO_CREATE)
        startService(serviceintent)

        PlayermusicList.addAll(MainActivity.musicList)
        position=intent.getIntExtra("index",0)

        Log.d("position",position.toString())

//set the image for music
        setLayout()


//play and pause
        playerBinding.Pausebtn.setOnClickListener {

            if (isPlaying)
            {

                playerBinding.Pausebtn.setIconResource(R.drawable.playandpause)
                musicservice!!.mediaPlayer!!.pause()
                player.musicservice!!.showNotification()
                isPlaying=false


            }
            else
            {

                playerBinding.Pausebtn.setIconResource(R.drawable.pause)
                musicservice!!. mediaPlayer!!.start()
                player.musicservice!!.showNotification()
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



    private fun setMediaPlayer() {


        musicservice!!. mediaPlayer?.let {
            it.stop()
            it.reset()
            it.release()
        }
        musicservice!!. mediaPlayer = null

        musicservice!!.mediaPlayer= MediaPlayer()
        musicservice!!. mediaPlayer!!.reset()
        musicservice!!.mediaPlayer!!.setDataSource(PlayermusicList[position].path)
        musicservice!!.mediaPlayer!!.prepare()
        musicservice!!.mediaPlayer!!.start()

        playerBinding.Pausebtn.setIconResource(R.drawable.pause)
        player.musicservice!!.showNotification()
        isPlaying=true


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

//    override fun onDestroy() {
//        super.onDestroy()
//
//        musicservice !!.mediaPlayer?.let {
//            it.stop()
//            it.reset()
//            it.release()
//        }
//        musicservice!!. mediaPlayer = null
//
//    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

        val binder= p1 as musicService.Mybinder
        musicservice=binder.currentService()
        setMediaPlayer()
        musicservice!!.showNotification()

    }

    override fun onServiceDisconnected(p0: ComponentName?) {

        musicservice=null
    }


    override fun onNewIntent(uiintent: Intent) {
        super.onNewIntent(uiintent)

        when(uiintent.action){

            "Update_the_UI"->{
                var index: Int= uiintent.getIntExtra("position",0)
        Glide.with(this).load(PlayermusicList[index].imageuri).apply(RequestOptions.placeholderOf(R.drawable.musical_icon))
            .centerCrop().into(playerBinding.shapeableImageView)
        playerBinding.Pausebtn.setIconResource(R.drawable.pause)
        playerBinding.appCompatTextView.text=PlayermusicList[index].title.toString()

            }

            "previous_ui_update"->{
                var index: Int= uiintent.getIntExtra("previous",0)
                Glide.with(this).load(PlayermusicList[index].imageuri).apply(RequestOptions.placeholderOf(R.drawable.musical_icon))
                    .centerCrop().into(playerBinding.shapeableImageView)
                playerBinding.Pausebtn.setIconResource(R.drawable.pause)
                playerBinding.appCompatTextView.text=PlayermusicList[index].title.toString()
            }
        }
    }


}




