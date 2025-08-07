package com.albin.spotify.Views

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.albin.spotify.MainActivity
import com.albin.spotify.R
import com.albin.spotify.databinding.ActivitySinglePlaylistDetailsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class SinglePlaylistDetails : AppCompatActivity() {
    lateinit var adapter: MusicAdapter
   lateinit var binding: ActivitySinglePlaylistDetailsBinding
    companion object{

        var curentplayListPos:Int = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivitySinglePlaylistDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewP.setItemViewCacheSize(10)
        binding.recyclerViewP.layoutManager= LinearLayoutManager(this@SinglePlaylistDetails)

         adapter= MusicAdapter(this, createPlaylist.musicPlaylitObj.ref[curentplayListPos].playlist)
        binding.recyclerViewP.adapter=adapter


    }

    override fun onResume() {
        super.onResume()
        createPlaylist.musicPlaylitObj.ref[curentplayListPos].playlist.addAll(MainActivity.musicList)
        binding.playlistNameText.text=createPlaylist.musicPlaylitObj.ref[curentplayListPos].playlistname
        binding.totalSongsText.text="Total Songs:"+(adapter.itemCount).toString()

        if (adapter.itemCount > 0)
        {
            Glide.with(this).load(createPlaylist.musicPlaylitObj.ref[curentplayListPos].playlist[0].imageuri
                ).apply(RequestOptions()
                .placeholder(R.drawable.musical_icon))
                .circleCrop()
                .into(binding.playlistImage)

            binding.shuffleBtn.isVisible=true
        }


    }
}