package com.albin.spotify

import com.albin.spotify.Views.Favourites
import com.albin.spotify.Views.MoreOPtions
import com.albin.spotify.Views.player
import kotlinx.serialization.Serializable


@Serializable
data class Music (val id: String,val title:String, val singer: String, val duration:Long=0,val path:String,val imageuri: String) {

    fun formatDuration(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)


    }
}
fun favSongFind( Id:String): Int {

    Favourites.FavMusicList.forEachIndexed { index,music->

        if( music.id == Id){

            return index
        }
    }
    return -1
}