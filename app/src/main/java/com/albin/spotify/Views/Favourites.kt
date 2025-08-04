package com.albin.spotify.Views

import android.os.Bundle
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

class Favourites : AppCompatActivity() {

    lateinit var favBinding: ActivityFavouritesBinding
    lateinit var adapter: FavouriteAdapter

    companion object{
        var FavMusicList: ArrayList<Music> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        favBinding= ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(favBinding.root)

        favBinding.recyclerViewF.layoutManager= GridLayoutManager(this@Favourites,4)

        adapter = FavouriteAdapter(this@Favourites,FavMusicList)

        favBinding.recyclerViewF.adapter=adapter

    }
}