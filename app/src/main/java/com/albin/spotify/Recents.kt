package com.albin.spotify

import android.R.attr.nestedScrollingEnabled
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.albin.spotify.Views.RecnetsAdapter
import com.albin.spotify.databinding.ActivityRecentsBinding

class Recents : AppCompatActivity() {

    lateinit var Rbinding: ActivityRecentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Rbinding= ActivityRecentsBinding.inflate(layoutInflater)
        setContentView(Rbinding.root)

        //jump back in adapter

        val selected: ArrayList<Music> = MainActivity.musicList.shuffled().take(4) as ArrayList<Music>
        var jumpBackInAdapter: RecnetsAdapter= RecnetsAdapter(this@Recents,selected)
       Rbinding.recyclerView1.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
           adapter=jumpBackInAdapter
        }

        //chill
        val selected2: ArrayList<Music> = MainActivity.musicList.shuffled().take(4) as ArrayList<Music>
        var chillAdapter: RecnetsAdapter= RecnetsAdapter(this@Recents,selected2)
        Rbinding.recyclerView2.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter=chillAdapter
        }
        //recents

        val selected3: ArrayList<Music> = MainActivity.musicList.shuffled().take(4) as ArrayList<Music>
        var recentAdapter: RecnetsAdapter= RecnetsAdapter(this@Recents,selected3)
        Rbinding.recyclerView3.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter=recentAdapter
        }

        //more of what u like

        val selected4: ArrayList<Music> = MainActivity.musicList.shuffled().take(4) as ArrayList<Music>
        var moreadapter: RecnetsAdapter= RecnetsAdapter(this@Recents,selected4)
        Rbinding.recyclerView4.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter=moreadapter
        }

        //recommended for today

        val selected5: ArrayList<Music> = MainActivity.musicList.shuffled().take(4) as ArrayList<Music>
        var recAdapter: RecnetsAdapter= RecnetsAdapter(this@Recents,selected5)
        Rbinding.recyclerView5.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter=recAdapter
        }



        //neew release for you

        val selected6: ArrayList<Music> = MainActivity.musicList.shuffled().take(4) as ArrayList<Music>
        var newRelseAdapter: RecnetsAdapter= RecnetsAdapter(this@Recents,selected6)
        Rbinding.recyclerView6.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter=newRelseAdapter
        }
    }
}