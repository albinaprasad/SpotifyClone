package com.albin.spotify.Views

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.albin.spotify.FavouriteAdapter
import com.albin.spotify.MainActivity
import com.albin.spotify.Music
import com.albin.spotify.R
import com.albin.spotify.databinding.ActivityFavouritesBinding
import com.google.gson.reflect.TypeToken

class Favourites : AppCompatActivity() {

    lateinit var favBinding: ActivityFavouritesBinding
    lateinit var adapter: FavouriteAdapter

    companion object{
        var FavMusicList: ArrayList<Music> = ArrayList()
        var isGrid:Boolean = true
        var isShuffle=false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        favBinding= ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(favBinding.root)

        favBinding.recyclerViewF.layoutManager= GridLayoutManager(this@Favourites,4)

        adapter = FavouriteAdapter(this@Favourites,FavMusicList)

        favBinding.recyclerViewF.adapter=adapter




        //grid and list layout setup

        favBinding.layoutChangeBtn.setOnClickListener {

            if (isGrid)
            {
                isGrid=false
                favBinding.recyclerViewF.layoutManager = LinearLayoutManager(this)
                favBinding.layoutChangeBtn.setImageResource(R.drawable.list_view)

            }
            else{
                isGrid=true
                favBinding.recyclerViewF.layoutManager= GridLayoutManager(this@Favourites,4)
                favBinding.layoutChangeBtn.setImageResource(R.drawable.grid_view)
            }

        }

        favBinding.shuffleBtn.setOnClickListener {

            FavMusicList.shuffle()
            favBinding.recyclerViewF.adapter?.notifyDataSetChanged()

            Toast.makeText(applicationContext,"Favourites shuffled ", Toast.LENGTH_SHORT).show()
        }

    }
}