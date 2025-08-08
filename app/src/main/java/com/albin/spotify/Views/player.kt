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
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.media.MediaTimestamp
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Debug
import android.os.IBinder
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.albin.spotify.MainActivity
import com.albin.spotify.Music
import com.albin.spotify.NotificationReciever
import com.albin.spotify.R
import com.albin.spotify.Views.SinglePlaylistDetails.Companion.curentplayListPos
import com.albin.spotify.databinding.ActivityPlayerBinding
import com.albin.spotify.favSongFind
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions



@SuppressLint("NewApi")
class player : AppCompatActivity(), ServiceConnection, MediaPlayer.OnCompletionListener {


    companion object{
        var PlayermusicList = ArrayList<Music>()
        var position:Int=0
        var musicservice : musicService?=null
        var isPlaying: Boolean=false
        var isRepeat:Boolean=false


        lateinit var playerBinding: ActivityPlayerBinding

    }

    var startY:Float=0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        playerBinding= ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(playerBinding.root)

        val whiteColor = ContextCompat.getColor(this, R.color.white)
        val greenColor = ContextCompat.getColor(this, R.color.green)



        // starting the music service

        if (intent.action=="mainIntent")
        {
            PlayermusicList.clear()
            PlayermusicList.addAll(MainActivity.musicList)
            position=intent.getIntExtra("index",0)
            val serviceintent= Intent(this, musicService::class.java)
            bindService(serviceintent,this,BIND_AUTO_CREATE)
            startService(serviceintent)
            Log.d("position",position.toString())
        }


        if (intent.action=="FavAdapter")
        {
            val intent= Intent(this, musicService::class.java)
            bindService(intent,this,BIND_AUTO_CREATE)
            startService(intent)
            Toast.makeText(this@player,"player clicked",Toast.LENGTH_SHORT).show()
            PlayermusicList.clear()
            PlayermusicList.addAll(Favourites.FavMusicList)
            //setMediaPlayer()
        }

        if(intent.action == "PLAYLIST") {
            val intent= Intent(this, musicService::class.java)
            bindService(intent,this,BIND_AUTO_CREATE)
            startService(intent)

            var pos = intent.getIntExtra("playlistIndex",0)
            position = intent.getIntExtra("index", 0)

            Toast.makeText(this@player,"player clicked",Toast.LENGTH_SHORT).show()
            PlayermusicList.clear()
            PlayermusicList.addAll(createPlaylist.musicPlaylitObj.ref[pos].playlist)
            }


        val serviceintent = Intent(this, musicService::class.java)
        bindService(serviceintent, this, BIND_AUTO_CREATE)
        startService(serviceintent)

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

            if(isRepeat== false)
            {

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



        }

        //previous button
    playerBinding.backbtn.setOnClickListener {


        if(isRepeat== false)
        {
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
}


        //back navigation
        playerBinding.Homebtn.setOnClickListener {

            Log.d("ButtonDebug", "Button clicked!")
            finish()
        }

        //swipe from top to down to go to the mainActivity
        swipeToGoBack()

        playerBinding.shareBtn.setOnClickListener { view ->
            view.animate()
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(100)
                .withEndAction {
                    view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                }
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "audio/*"
                putExtra(Intent.EXTRA_STREAM, Uri.parse(PlayermusicList[position].path))
            }
            startActivity(Intent.createChooser(shareIntent, "Sharing Music"))
        }
        //seekbar setup
        playerBinding.musicSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(
                p0: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if(fromUser)
                {
                    musicservice!!.mediaPlayer!!.seekTo(progress)
                    playerBinding.currentTimeTextView.text= formatTimeDuration(musicservice!!.mediaPlayer!!.currentPosition.toLong())
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

//repeat button feature

        playerBinding.repeatBtn.setOnClickListener {

            if (isRepeat==false)
            {
                isRepeat=true

                playerBinding.repeatBtn.setColorFilter(greenColor, PorterDuff.Mode.SRC_IN)
            }
            else
            {
                isRepeat=false


                playerBinding.repeatBtn.setColorFilter(whiteColor, PorterDuff.Mode.SRC_IN)
            }
        }

        // 3 dots
        playerBinding.favBtn.setOnClickListener {
            val moreOptionsIntent=Intent(this, MoreOPtions::class.java)
            moreOptionsIntent.putExtra("position",position)
            startActivity(moreOptionsIntent)
        }

        //sleeper setup

        playerBinding.timerBtn.setOnClickListener {

            val timerIntent= Intent(this, SleepTimer::class.java)
            timerIntent.putExtra("duration",PlayermusicList[position].duration)

            Log.d("Time ","duration send"+PlayermusicList[position].duration.toString())

            startActivity(timerIntent)
        }
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

        playerBinding.currentTimeTextView.text= formatTimeDuration(musicservice!!.mediaPlayer!!.currentPosition.toLong())

        playerBinding.totalTimeTextView.text= formatTimeDuration(musicservice!!.mediaPlayer!!.duration.toLong())

        playerBinding.musicSeekBar.progress=0
        playerBinding.musicSeekBar.max=musicservice!!.mediaPlayer!!.duration

        musicservice!!.mediaPlayer!!.setOnCompletionListener(this)


    }

     fun formatTimeDuration(lng: Long): String {
        val totalSeconds = lng / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

     fun setLayout() {

        Glide.with(this).load(PlayermusicList[position].imageuri).apply(RequestOptions.placeholderOf(R.drawable.musical_icon))
            .centerCrop().into(playerBinding.shapeableImageView)

        playerBinding.appCompatTextView.text=PlayermusicList[position].title.toString()
         playerBinding.appCompatTextView.setSelected(true)

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
        musicservice!!.seekbarSetup()

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

    override fun onCompletion(p0: MediaPlayer?) {

        if(isRepeat==false)
        {
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
        else
        {
            setMediaPlayer()
            setLayout()
        }
    }




}




