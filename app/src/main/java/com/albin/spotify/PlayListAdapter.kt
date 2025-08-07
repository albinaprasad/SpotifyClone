package com.albin.spotify

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.albin.spotify.Views.Playlists
import com.albin.spotify.Views.SinglePlaylistDetails
import com.albin.spotify.Views.SinglePlaylistDetails.Companion.curentplayListPos
import com.albin.spotify.Views.createPlaylist
import com.albin.spotify.databinding.PlaylistItemsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class PlayListAdapter(var context: Context,var PlaylistsList: ArrayList<Playlist>,): RecyclerView.Adapter<PlayListAdapter.playlistViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): playlistViewHolder {
       val view= PlaylistItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return playlistViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: playlistViewHolder,
        position: Int
    ) {

        holder.playlitsItemBinding.playLitname.text = PlaylistsList[position].playlistname.toString()
        holder.playlitsItemBinding.playDeleteBtn.setOnClickListener {

            val builder = AlertDialog.Builder(context)
            builder.setTitle(PlaylistsList[position].playlistname)
            builder.setMessage("Do you need to delete the Playlist?")
            builder.setPositiveButton("OK") { dialog, which ->

               createPlaylist.musicPlaylitObj.ref.removeAt(position)
                Playlists.playlistadapter.refreshPlaylistData()
            dialog.dismiss()
            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            

            builder.show()
        }

        holder.playlitsItemBinding.playlistImage.setOnClickListener {
            val singlePlaylistIntent = Intent(context, SinglePlaylistDetails::class.java)
            context.startActivity(singlePlaylistIntent)
        }

        if(createPlaylist.musicPlaylitObj.ref[position].playlist.size> 0)
        {

            Glide.with(context).load(createPlaylist.musicPlaylitObj.ref[curentplayListPos].playlist[0].imageuri
            ).apply(RequestOptions()
                .placeholder(R.drawable.musical_icon))
                .circleCrop()
                .into(holder.playlitsItemBinding.playlistImage)
            notifyDataSetChanged()
        }


    }

    override fun getItemCount(): Int {
      return PlaylistsList.size
    }


    class playlistViewHolder(val playlitsItemBinding: PlaylistItemsBinding): RecyclerView.ViewHolder(playlitsItemBinding.root){

    }


        fun refreshPlaylistData()
        {
            PlaylistsList= ArrayList()
            PlaylistsList.addAll(createPlaylist.musicPlaylitObj.ref)
            notifyDataSetChanged()
        }


}