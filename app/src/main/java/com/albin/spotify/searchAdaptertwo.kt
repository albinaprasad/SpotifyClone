package com.albin.spotify

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.content.ContextCompat

import com.albin.spotify.databinding.CardItemSearchTwoBinding
import androidx.recyclerview.widget.RecyclerView

class searchAdaptertwo(val context: Context,val dataList: ArrayList<Carditems>): RecyclerView.Adapter<searchAdaptertwo.serachviewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): serachviewHolder {
       val view= CardItemSearchTwoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return serachviewHolder(view)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(
        holder: serachviewHolder,
        position: Int
    ) {
        holder.cardBinding.textview.text=dataList[position].name.toString()
        holder.cardBinding.main.backgroundTintList = ContextCompat.getColorStateList(context, dataList[position].backgroundColor)
        holder.cardBinding.ImagView.setImageResource(dataList[position].image)

        //adding small zoom animation on to the cardview
        holder.cardBinding.main.setOnTouchListener { view,event->

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


    class serachviewHolder(val cardBinding: CardItemSearchTwoBinding):RecyclerView.ViewHolder(cardBinding.root){

    }
}