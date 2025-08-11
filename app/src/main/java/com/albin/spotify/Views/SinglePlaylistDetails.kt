package com.albin.spotify.Views

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.albin.spotify.MainActivity
import com.albin.spotify.Playlist
import com.albin.spotify.R
import com.albin.spotify.SongSelectionAdapter
import com.albin.spotify.Views.createPlaylist.Companion.musicPlaylitObj
import com.albin.spotify.databinding.ActivitySinglePlaylistDetailsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SinglePlaylistDetails : AppCompatActivity() {
    lateinit var adapter: MusicAdapter
    lateinit var binding: ActivitySinglePlaylistDetailsBinding

    companion object {
        var curentplayListPos: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {

//data loading
        val sharedPref: SharedPreferences = getSharedPreferences("PLAYLISTS", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("playlistsKey", null)

        if (json != null) {
            val type = object : TypeToken<ArrayList<Playlist>>() {}.type
            val loadedPlaylists: ArrayList<Playlist>? = gson.fromJson(json, type)
            if (loadedPlaylists != null) {
                createPlaylist.musicPlaylitObj.ref=loadedPlaylists
            }
        }




        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySinglePlaylistDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewP.setItemViewCacheSize(10)
        binding.recyclerViewP.layoutManager = LinearLayoutManager(this@SinglePlaylistDetails)

        adapter =
            MusicAdapter(this, createPlaylist.musicPlaylitObj.ref[curentplayListPos].playlist, true)
        binding.recyclerViewP.adapter = adapter


        //ADD SOND BUTTON

        binding.addSong.setOnClickListener {
            val addsongIntent = Intent(this@SinglePlaylistDetails, songSelection::class.java)
            startActivity(addsongIntent)
        }

        //delete al btn
        binding.removeAll.setOnClickListener {

            val builder = AlertDialog.Builder(this@SinglePlaylistDetails)
            builder.setTitle("Remove all songs")
            builder.setMessage("Do you need to remove all song from the Playlist?")
            builder.setPositiveButton("OK") { dialog, which ->
                createPlaylist.musicPlaylitObj.ref[SinglePlaylistDetails.curentplayListPos].playlist.clear()
                dialog.dismiss()
                funSavePlaylist()
                adapter.notifyDataSetChanged()


            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()

        }
    }

    override fun onResume() {
        super.onResume()
        binding.playlistNameText.text =
            createPlaylist.musicPlaylitObj.ref[curentplayListPos].playlistname
        binding.totalSongsText.text = "Total Songs:" + (adapter.itemCount).toString()

        if (adapter.itemCount > 0) {
            Glide.with(this).load(
                createPlaylist.musicPlaylitObj.ref[curentplayListPos].playlist[0].imageuri
            ).apply(
                RequestOptions()
                    .placeholder(R.drawable.musical_icon)
            )
                .circleCrop()
                .into(binding.playlistImage)

            binding.shuffleBtn.isVisible = true
        }
        adapter.notifyDataSetChanged()


    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

     fun funSavePlaylist() {
        val sharedPref: SharedPreferences = this.getSharedPreferences("PLAYLISTS", MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        val json = gson.toJson(musicPlaylitObj.ref)
        editor.putString("playlistsKey", json)
        editor.apply()
    }


}