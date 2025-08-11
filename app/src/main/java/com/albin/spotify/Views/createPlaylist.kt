package com.albin.spotify.Views

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.albin.spotify.MusicPlaylit
import com.albin.spotify.Playlist
import com.albin.spotify.Views.createPlaylist.Companion.musicPlaylitObj
import com.albin.spotify.databinding.PlaylistDialogBinding
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class createPlaylist: AppCompatActivity() {

    lateinit var createBinding: PlaylistDialogBinding

    companion object{

        var musicPlaylitObj= MusicPlaylit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        createBinding = PlaylistDialogBinding.inflate(layoutInflater)
        setContentView(createBinding.root)


        //cancel btn
        createBinding.cancelBtn.setOnClickListener {
            finish()
        }

        //create btn

        createBinding.createBtn.setOnClickListener {

            var isPlayListExists=false

            var playlistNameByuser=createBinding.playlistET.text.toString()

            Log.d("name",playlistNameByuser)

            if (playlistNameByuser!= null && playlistNameByuser.isNotEmpty())
            {
                for (list in musicPlaylitObj.ref)
                {
                    if (playlistNameByuser == list.playlistname)
                    {
                        isPlayListExists=true
                        break
                    }
                }
                if (isPlayListExists)
                {
                    Toast.makeText(this,"playlist already exixts", Toast.LENGTH_SHORT).show()
                }
                else{
                    var playlistObj= Playlist()
                    playlistObj.playlistname=playlistNameByuser.toString()
                    playlistObj.playlist= ArrayList()

                    var calendar=java.util.Calendar.getInstance()
                    val sdf= SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)

                    playlistObj.createdon=sdf.format(calendar.time)
                    musicPlaylitObj.ref.add(playlistObj)

                    funSavePlaylist()
                    finish()
                }

            }
        }

    }
}

private fun createPlaylist.funSavePlaylist() {
    val sharedPref: SharedPreferences = this.getSharedPreferences("PLAYLISTS", MODE_PRIVATE)
    val editor = sharedPref.edit()
    val gson = Gson()
    val json = gson.toJson(musicPlaylitObj.ref)
    editor.putString("playlistsKey", json)
    editor.apply()
}
