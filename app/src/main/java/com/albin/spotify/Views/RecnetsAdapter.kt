package com.albin.spotify.Views

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albin.spotify.Music
import com.albin.spotify.R
import com.albin.spotify.databinding.JumpBackInBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class RecnetsAdapter(val context: Context,var recentPlaylists: ArrayList<Music>): RecyclerView.Adapter<RecnetsAdapter.recentViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): recentViewHolder {

        val view= JumpBackInBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return recentViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: recentViewHolder,
        position: Int
    ) {
        Glide.with(context).load(
            recentPlaylists[position]
                .imageuri
        ).apply(
            RequestOptions()
                .placeholder(R.drawable.musical_icon)
        )
            .into(holder.jumpBackInBinding.songIMG)


        holder.jumpBackInBinding.songName.text=recentPlaylists[position].title.toString()

    }



    override fun getItemCount(): Int {
       return recentPlaylists.size
    }


    class recentViewHolder(val jumpBackInBinding: JumpBackInBinding): RecyclerView.ViewHolder(jumpBackInBinding.root){

    }
}