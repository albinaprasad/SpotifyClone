package com.albin.spotify

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.albin.spotify.Views.Playlists
import com.albin.spotify.Views.SinglePlaylistDetails
import com.albin.spotify.Views.createPlaylist
import com.albin.spotify.databinding.PlaylistItemsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class PlayListAdapter(var context: Context, var PlaylistsList: ArrayList<Playlist>) :
    RecyclerView.Adapter<PlayListAdapter.PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = PlaylistItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.playlitsItemBinding.playLitname.text =
            PlaylistsList[position].playlistname.toString()

        // Delete button
        holder.playlitsItemBinding.playDeleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(PlaylistsList[position].playlistname)
            builder.setMessage("Do you need to delete the Playlist?")
            builder.setPositiveButton("OK") { dialog, which ->
                createPlaylist.musicPlaylitObj.ref.removeAt(position)

                if (SinglePlaylistDetails.curentplayListPos >= createPlaylist.musicPlaylitObj.ref.size) {
                    SinglePlaylistDetails.curentplayListPos = 0
                }
                refreshPlaylistData()
                dialog.dismiss()
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
        }

        holder.playlitsItemBinding.playlistImage.setOnClickListener {
            SinglePlaylistDetails.curentplayListPos = position // Set the correct playlist position
            val singlePlaylistIntent = Intent(context, SinglePlaylistDetails::class.java)
            context.startActivity(singlePlaylistIntent)
        }

        // Load playlist image

        if (createPlaylist.musicPlaylitObj.ref[position].playlist.isNotEmpty()) {
            Glide.with(context)
                .load(createPlaylist.musicPlaylitObj.ref[position].playlist[0].imageuri)
                .apply(RequestOptions().placeholder(R.drawable.musical_icon))
                .circleCrop()
                .into(holder.playlitsItemBinding.playlistImage)
        } else {
            // Load default image if playlist is empty
            Glide.with(context)
                .load(R.drawable.musical_icon)
                .circleCrop()
                .into(holder.playlitsItemBinding.playlistImage)
        }
    }

    override fun getItemCount(): Int {
        return PlaylistsList.size
    }

    class PlaylistViewHolder(val playlitsItemBinding: PlaylistItemsBinding) :
        RecyclerView.ViewHolder(playlitsItemBinding.root)

    fun refreshPlaylistData() {
        PlaylistsList = ArrayList()
        PlaylistsList.addAll(createPlaylist.musicPlaylitObj.ref)
        notifyDataSetChanged() // Safe to call here, outside of layout computation
    }
}