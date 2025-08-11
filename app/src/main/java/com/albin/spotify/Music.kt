package com.albin.spotify

import com.albin.spotify.Views.Favourites
import com.albin.spotify.Views.MoreOPtions
import com.albin.spotify.Views.player
import kotlinx.serialization.Serializable
import java.io.File


data class Music(
    val id: String,
    val title: String,
    val singer: String,
    val duration: Long = 0,
    val path: String,
    val imageuri: String
) {


    fun formatDuration(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}

class Playlist {
    lateinit var playlistname: String
    lateinit var playlist: ArrayList<Music>
    lateinit var createdon: String
}


class MusicPlaylit {
    var ref = ArrayList<Playlist>()
}

fun favSongFind(Id: String): Int {

    Favourites.FavMusicList.forEachIndexed { index, music ->

        if (music.id == Id) {

            return index
        }
    }
    return -1
}

fun checkMusicExists(musicData: ArrayList<Music>): ArrayList<Music> {

    musicData.forEachIndexed { index, music ->
        var file = File(music.path)
        if (file.exists() == false) {
            musicData.removeAt(index)
        }
    }

    return musicData
}