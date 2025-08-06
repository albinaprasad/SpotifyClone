package com.albin.spotify

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albin.spotify.databinding.PlaylistItemsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class PlayListAdapter(var context: Context,var PlaylistsList: List<Music>): RecyclerView.Adapter<PlayListAdapter.playlistViewHolder>(){
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
        Glide.with(context).load(PlaylistsList[position]
            .imageuri).apply(RequestOptions()
            .placeholder(R.drawable.musical_icon))
            .circleCrop()
            .into(holder.playlitsItemBinding.playlistImage)
    }

    override fun getItemCount(): Int {
      return PlaylistsList.size
    }


    class playlistViewHolder(val playlitsItemBinding: PlaylistItemsBinding): RecyclerView.ViewHolder(playlitsItemBinding.root){

    }
}