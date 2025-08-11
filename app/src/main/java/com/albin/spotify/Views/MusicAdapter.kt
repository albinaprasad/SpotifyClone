package com.albin.spotify.Views

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albin.spotify.Music
import com.albin.spotify.R
import com.albin.spotify.databinding.SongDetailsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MusicAdapter(
    val context: Context,
    var musicList: ArrayList<Music>,
    var plalist: Boolean = false
) : RecyclerView.Adapter<MusicAdapter.musicViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): musicViewHolder {
        val view = SongDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return musicViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: musicViewHolder,
        position: Int
    ) {
        holder.musicBinding.songName.text = musicList[position].title // Set song title
        holder.musicBinding.songDetails.text = musicList[position].singer // Placeholder
        holder.musicBinding.duration.text =
            musicList[position].formatDuration(musicList[position].duration)

        Glide.with(context).load(
            musicList[position]
                .imageuri
        ).apply(
            RequestOptions()
                .placeholder(R.drawable.musical_icon)
        )
            .circleCrop()
            .into(holder.musicBinding.albumArt)

        //click on the cardview to make the music-player activity
        holder.musicBinding.card.setOnClickListener {

            when {
                plalist -> {
                    val plaintent = Intent(context, player::class.java)
                    plaintent.putExtra("playlistIndex", SinglePlaylistDetails.curentplayListPos)
                    plaintent.putExtra("index", player.Companion.position)
                    plaintent.setAction("PLAYLIST")

                    context.startActivity(plaintent)
                }

                else -> {
                    val intent = Intent(context, player::class.java)
                    intent.putExtra("index", position)
                    intent.setAction("mainIntent")
                    Log.d("position", position.toString())
                    context.startActivity(intent)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }


    class musicViewHolder(val musicBinding: SongDetailsBinding) :
        RecyclerView.ViewHolder(musicBinding.root) {
        var title = musicBinding.songName
        var musicDetails = musicBinding.songDetails
        var image = musicBinding.albumArt
        var time = musicBinding.duration
    }

}