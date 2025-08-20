package com.albin.spotify

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(
        holder: searchViewHiolder,
        position: Int
    ) {

        holder.serachCardBinding.textview.text=dataList[position].name.toString()
        holder.serachCardBinding.main.backgroundTintList = ContextCompat.getColorStateList(context, dataList[position].backgroundColor)
        holder.serachCardBinding.ImagView.setImageResource(dataList[position].image)

        holder.serachCardBinding.main.setOnTouchListener { view,event->

            when(event.action){

                MotionEvent.ACTION_DOWN->{
                    view.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).start()

                }
                MotionEvent.ACTION_CANCEL,MotionEvent.ACTION_UP->{
                    view.animate().scaleX(1f).scaleY(1f).setDuration(100).start()

                }

            }
            true
        }

    }

    override fun getItemCount(): Int {
       return dataList.size
    }


    class searchViewHiolder(val serachCardBinding: SearchCardviewOneBinding): RecyclerView.ViewHolder(serachCardBinding.root){

    }
}