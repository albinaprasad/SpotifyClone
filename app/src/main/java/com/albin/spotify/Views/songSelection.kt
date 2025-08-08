package com.albin.spotify.Views

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.albin.spotify.MainActivity
import com.albin.spotify.Music
import com.albin.spotify.R
import com.albin.spotify.SongSelectionAdapter
import com.albin.spotify.databinding.ActivitySongSelectionBinding

class songSelection : AppCompatActivity() {

    lateinit var selectBinding: ActivitySongSelectionBinding
    lateinit var selectAdapter: SongSelectionAdapter

    var AllSongsArray= ArrayList<Music>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        selectBinding= ActivitySongSelectionBinding.inflate(layoutInflater)
        setContentView(selectBinding.root)

        //adapter setUP
        selectAdapter= SongSelectionAdapter(this@songSelection, MainActivity.musicList)
        selectBinding.recyclerViewS.setItemViewCacheSize(10)
        selectBinding.recyclerViewS.layoutManager= LinearLayoutManager(this)
        selectBinding.recyclerViewS.adapter=selectAdapter

        AllSongsArray.addAll(MainActivity.musicList)

        //searchview set up

        selectBinding.searchViewS.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                MainActivity.musicList.clear()
                if(newText.isNullOrEmpty())
                {
                    MainActivity.musicList.addAll(AllSongsArray)
                }
                else{
                    var query=newText.lowercase()

                    for(song in AllSongsArray)
                    {
                        var singername=song.singer.lowercase()
                        var songname=song.title.lowercase()

                        if (songname.contains(query) || singername.contains(query))
                        {
                            MainActivity.musicList.add(song)
                        }
                    }

                }
                selectAdapter.notifyDataSetChanged()

              return true
            }


        })





    }
}