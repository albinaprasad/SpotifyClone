package com.albin.spotify.Views

import android.R.attr.spacing
import android.app.Activity
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.albin.spotify.Carditems
import com.albin.spotify.R
import com.albin.spotify.databinding.ActivitySearchBinding
import com.albin.spotify.serachCardAdapter

class Search : AppCompatActivity() {
    var carditems = ArrayList<Carditems>()
    lateinit var searchBinding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchBinding.root)

//first 4 card view setup
        carditems.add(Carditems("Muisc", R.color.pink, R.drawable.singer))
        carditems.add(Carditems("Podcasts", R.color.Darkgreen, R.drawable.science))
        carditems.add(Carditems("Live Events", R.color.violet, R.drawable.singere_male))
        carditems.add(Carditems("Home of\nI-Pop", R.color.Dblue, R.drawable.green_symbol))


        val adapterOne= serachCardAdapter(this@Search,carditems)
        searchBinding.recyclerView1.layoutManager= GridLayoutManager(this,2)
        searchBinding.recyclerView1.adapter=adapterOne

//vide view set up

        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.video_one}")
        searchBinding.video1.setVideoURI(videoUri)

        searchBinding.video1.setOnPreparedListener { mediaPlayer ->
            // Disable sound
           mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT)
            mediaPlayer.setVolume(0f, 0f)

            // Enable looping
            mediaPlayer.isLooping = true


            // Start playing
            searchBinding.video1.start()
        }


    }
}