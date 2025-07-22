package com.albin.spotify

data class Music (val id: String,val title:String, val singer: String, val duration:Long=0,val path:String,val imageuri: String){

    fun formatDuration(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

}