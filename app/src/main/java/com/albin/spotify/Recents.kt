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

    companion object {
        var jumpBackIn = ArrayList<Music>()
        var chill = ArrayList<Music>()
        var recents = ArrayList<Music>()
        var moreOfWhat = ArrayList<Music>()
        var recomendedForToday = ArrayList<Music>()
        var newRelease = ArrayList<Music>()


        val JUMP: String = "Jump"
        val CHILL: String = "Chill"
        val RECENTS: String = "Rececnts"
        val MOREOF: String = "moreofWhat"
        val RECOMENDED: String = "RecomendedForToday"
        var NEWRELEASE: String = "newRelease"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Rbinding = ActivityRecentsBinding.inflate(layoutInflater)
        setContentView(Rbinding.root)

        //jump back in adapter

        jumpBackIn = MainActivity.musicList.shuffled().take(4) as ArrayList<Music>
        var jumpBackInAdapter: RecnetsAdapter = RecnetsAdapter(this@Recents, jumpBackIn,JUMP)
        Rbinding.recyclerView1.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = jumpBackInAdapter
        }

        //chill
        chill = MainActivity.musicList.shuffled().take(4) as ArrayList<Music>
        var chillAdapter: RecnetsAdapter = RecnetsAdapter(this@Recents, chill,CHILL)
        Rbinding.recyclerView2.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = chillAdapter
        }
        //recents

        recents = MainActivity.musicList.shuffled().take(4) as ArrayList<Music>
        var recentAdapter: RecnetsAdapter = RecnetsAdapter(this@Recents, recents,RECENTS)
        Rbinding.recyclerView3.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = recentAdapter
        }

        //more of what u like

        moreOfWhat = MainActivity.musicList.shuffled().take(4) as ArrayList<Music>
        var moreadapter: RecnetsAdapter = RecnetsAdapter(this@Recents, moreOfWhat,MOREOF)
        Rbinding.recyclerView4.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = moreadapter
        }

        //recommended for today

        recomendedForToday = MainActivity.musicList.shuffled().take(4) as ArrayList<Music>
        var recAdapter: RecnetsAdapter = RecnetsAdapter(this@Recents, recomendedForToday,RECOMENDED)
        Rbinding.recyclerView5.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = recAdapter
        }


        //neew release for you

        newRelease = MainActivity.musicList.shuffled().take(4) as ArrayList<Music>
        var newRelseAdapter: RecnetsAdapter = RecnetsAdapter(this@Recents, newRelease,NEWRELEASE)
        Rbinding.recyclerView6.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = newRelseAdapter
        }
    }



}