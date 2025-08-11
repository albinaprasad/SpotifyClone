package com.albin.spotify

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.albin.spotify.Views.MusicAdapter.musicViewHolder
import com.albin.spotify.Views.SinglePlaylistDetails
import com.albin.spotify.Views.createPlaylist
import com.albin.spotify.Views.createPlaylist.Companion.musicPlaylitObj
import com.albin.spotify.Views.player
import com.albin.spotify.databinding.SongDetailsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson

class SongSelectionAdapter(var context: Context, var musicList: ArrayList<Music>) :
    RecyclerView.Adapter<SongSelectionAdapter.selecioViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): selecioViewHolder {
        val view = SongDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return selecioViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: selecioViewHolder,
        position: Int
    ) {

        holder.Sbinding.songName.text = musicList[position].title // Set song title
        holder.Sbinding.songDetails.text = musicList[position].singer // Placeholder
        holder.Sbinding.duration.text =
            musicList[position].formatDuration(musicList[position].duration)

        Glide.with(context).load(
            musicList[position]
                .imageuri
        ).apply(
            RequestOptions()
                .placeholder(R.drawable.musical_icon)
        )
            .circleCrop()
            .into(holder.Sbinding.albumArt)

        //click on the cardview to make the music-player activity
        holder.Sbinding.card.setOnClickListener {

            if (addSong(musicList[position])) {
                holder.Sbinding.mainLL.setBackgroundColor(Color.parseColor("#00FF00"));
            } else {
                holder.Sbinding.mainLL.setBackgroundColor(Color.parseColor("#121212"))
            }
            funSavePlaylist()
            Toast.makeText(context, "Selection adaptyer clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }


    class selecioViewHolder(var Sbinding: SongDetailsBinding) :
        RecyclerView.ViewHolder(Sbinding.root) {
    }

    fun addSong(Song: Music): Boolean {

        createPlaylist.musicPlaylitObj.ref[SinglePlaylistDetails.curentplayListPos].playlist.forEachIndexed { index, music ->

            if (Song.id == music.id) {
                createPlaylist.musicPlaylitObj.ref[SinglePlaylistDetails.curentplayListPos].playlist.removeAt(
                    index
                )

                return false
            }
        }
        createPlaylist.musicPlaylitObj.ref[SinglePlaylistDetails.curentplayListPos].playlist.add(
            Song
        )
        return true
    }

    fun funSavePlaylist() {
        val sharedPref: SharedPreferences = context.getSharedPreferences("PLAYLISTS", MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        val json = gson.toJson(musicPlaylitObj.ref)
        editor.putString("playlistsKey", json)
        editor.apply()
    }
}