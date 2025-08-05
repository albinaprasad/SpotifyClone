package com.albin.spotify

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.albin.spotify.Views.Favourites
import com.albin.spotify.Views.player
import com.albin.spotify.databinding.FavouriteViewElementsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FavouriteAdapter(var context: Context,var musicList: List<Music>) : RecyclerView.Adapter<FavouriteAdapter.favViewBinder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): favViewBinder {

        val view= FavouriteViewElementsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return favViewBinder(view)
    }

    override fun onBindViewHolder(
        holder: favViewBinder,
        position: Int
    ) {
        Glide.with(context).load(musicList[position]
            .imageuri).apply(RequestOptions()
            .placeholder(R.drawable.musical_icon))
            .circleCrop()
            .into(holder.favBinding.songImgF)

        holder.favBinding.songNameF.text=musicList[position].title

        holder.favBinding.songImgF.setOnClickListener {

            var favAdapterIntent=Intent(context,player::class.java)
            favAdapterIntent.putExtra("favSongIndex",position)
            favAdapterIntent.setAction("FavAdapter")
            favAdapterIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            context.startActivity(favAdapterIntent)

        }


    }

    override fun getItemCount(): Int {
        return musicList.size
    }


    class favViewBinder(val favBinding: FavouriteViewElementsBinding):RecyclerView.ViewHolder(favBinding.root){

        var root=favBinding.root
    }
}