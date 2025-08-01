package com.albin.spotify

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albin.spotify.databinding.BtItemsBinding

class BTAdapter(val context: Context,val paired_devices: ArrayList<BTPaired>): RecyclerView.Adapter<BTAdapter.BTviewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BTviewHolder {
        val view= BtItemsBinding.inflate(LayoutInflater.from(context),parent,false)
        return BTviewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BTviewHolder,
        position: Int
    ) {
       holder.BTitembinding.BtdeviceName.text=paired_devices[position].deviceName.toString()
    }

    override fun getItemCount(): Int {
        return paired_devices.size
    }


    class BTviewHolder(val BTitembinding: BtItemsBinding): RecyclerView.ViewHolder(BTitembinding.root){

    }
}