package com.albin.spotify

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.albin.spotify.Views.RecnetsAdapter.recentViewHolder
import com.albin.spotify.databinding.JumpBackInBinding
import com.albin.spotify.databinding.SearchCardviewOneBinding

class serachCardAdapter(val context: Context,var dataList: ArrayList<Carditems>): RecyclerView.Adapter<serachCardAdapter.searchViewHiolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): searchViewHiolder {

        val view= SearchCardviewOneBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return searchViewHiolder(view)
    }

    override fun onBindViewHolder(
        holder: searchViewHiolder,
        position: Int
    ) {

        holder.serachCardBinding.textview.text=dataList[position].name.toString()
        holder.serachCardBinding.main.backgroundTintList = ContextCompat.getColorStateList(context, dataList[position].backgroundColor)
        holder.serachCardBinding.ImagView.setImageResource(dataList[position].image)

    }

    override fun getItemCount(): Int {
       return dataList.size
    }


    class searchViewHiolder(val serachCardBinding: SearchCardviewOneBinding): RecyclerView.ViewHolder(serachCardBinding.root){

    }
}