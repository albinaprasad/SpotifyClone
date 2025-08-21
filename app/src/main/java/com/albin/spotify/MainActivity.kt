package com.albin.spotify

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service.STOP_FOREGROUND_REMOVE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Carousel
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.albin.spotify.Views.Favourites
import com.google.gson.JsonSyntaxException
import com.albin.spotify.Views.MusicAdapter
import com.albin.spotify.Views.Playlists
import com.albin.spotify.Views.Search
import com.albin.spotify.Views.SinglePlaylistDetails
import com.albin.spotify.Views.createPlaylist
import com.albin.spotify.Views.player
import com.albin.spotify.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 100
    private val CAMERA_REQUEST_CODE = 200

    companion object {
        val musicList = ArrayList<Music>()
    }

    lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        // Request permission for the storage access
        requestPermission()

        //camrea permission
        request_cameraPermission()


//permission request for phone

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                800
            )
        }



        Favourites.FavMusicList = ArrayList()
        var editor: SharedPreferences = getSharedPreferences("Favourite", MODE_PRIVATE)
        var typeToken = object : TypeToken<ArrayList<Music>>() {}.type
        val jsonString = editor.getString("Fav_songs", null)

        if (jsonString != null) {
            val data: ArrayList<Music> =
                GsonBuilder().setLenient().create().fromJson(jsonString, typeToken)
            Favourites.FavMusicList.addAll(data)
        }


        //load the playlists
        val sharedPref: SharedPreferences = getSharedPreferences("PLAYLISTS", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("playlistsKey", null)

        if (json != null) {
            val type = object : TypeToken<ArrayList<Playlist>>() {}.type
            val loadedPlaylists: ArrayList<Playlist>? = gson.fromJson(json, type)
            if (loadedPlaylists != null) {
                createPlaylist.musicPlaylitObj.ref = loadedPlaylists
            }
        }


//playlits adding

        mainBinding.PlayListBtn.setOnClickListener {
            val playListintent = Intent(this@MainActivity, Playlists::class.java)
            startActivity(playListintent)
        }


        // Navigation bar set up
        mainBinding.navButton.setOnClickListener {
            mainBinding.drawerlayout.openDrawer(GravityCompat.START)
        }

        mainBinding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_addprofile -> {
                    ToastShower("Add account")
                }

                R.id.nav_whatsnew -> {
                    ToastShower("What's new clicked")
                }

                R.id.nav_recents -> {

                    val recentIntent=Intent(this@MainActivity, Recents::class.java)
                    startActivity(recentIntent)
                    //ToastShower("Recents clicked")
                }

                R.id.nav_updates -> {
                    ToastShower("Updates clicked")
                }

                R.id.nav_search->{
                    val serachIntent=Intent(this@MainActivity, Search::class.java)
                    startActivity(serachIntent)
                }

                R.id.nav_settings -> {
                    ToastShower("Settings clicked")
                }

                R.id.nav_exit -> {
                    finish() // Add actual exit functionality
                }
            }
            mainBinding.drawerlayout.closeDrawer(GravityCompat.START)
            true
        }
        //navbar animation
        navbarAnimationSetUp()

        //shuffle BUtton

        mainBinding.shuffleBtn.setOnClickListener {
            musicList.shuffle()
            mainBinding.recyclerView.adapter?.notifyDataSetChanged()
            Toast.makeText(this, "Library shuffled", Toast.LENGTH_SHORT).show()
        }


        // Only setup adapter if permission is already granted
        if (checkPermission()) {
            adapterSetup()
        }


        //fav button features
        mainBinding.favBtn.setOnClickListener {

            val favIntent = Intent(this, Favourites::class.java)
            startActivity(favIntent)
        }

        //search activity
        mainBinding.searchBtn.setOnClickListener {
            val searchIntent=Intent(this, Search::class.java)
            startActivity(searchIntent)
           
        }

    }

    private fun request_cameraPermission() {
       val cameraPermisson=arrayOf(Manifest.permission.CAMERA)

        if(  cameraPermisson.all { ContextCompat.checkSelfPermission(applicationContext,it) == PackageManager.PERMISSION_GRANTED } ){

        }
        else{
            ActivityCompat.requestPermissions(this,cameraPermisson,CAMERA_REQUEST_CODE)
        }
    }

    fun navbarAnimationSetUp() {

        mainBinding.drawerlayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                var moveDistance = drawerView.width * slideOffset
                mainBinding.main.translationX = moveDistance
            }

            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerClosed(drawerView: View) {

            }

            override fun onDrawerStateChanged(newState: Int) {

            }
        })
    }

    private fun adapterSetup() {


            var readedmusiclist = readAllMusic()
            musicList.clear()
            musicList.addAll(readedmusiclist)

        val adapter = MusicAdapter(this, musicList)
        mainBinding.recyclerView.setItemViewCacheSize(10)
        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mainBinding.recyclerView.adapter = adapter


    }

    // Check if permission is granted
    val permission: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_AUDIO
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Request permission
    private fun requestPermission() {
        if (!checkPermission()) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), PERMISSION_REQUEST_CODE)
        } else {
            //Toast.makeText(this, "Storage access granted!", Toast.LENGTH_SHORT).show()
            adapterSetup()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this,
                    "Permission granted! Loading music...",
                    Toast.LENGTH_SHORT
                ).show()
                adapterSetup() // Load music after permission is granted
            } else {
                Toast.makeText(
                    this,
                    "Permission denied! Cannot access music files.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    @SuppressLint("Range")
    private fun readAllMusic(): ArrayList<Music> {
        val tempMusicList = ArrayList<Music>()

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0" +
                " AND " + MediaStore.Audio.Media.DATA + " NOT LIKE '%/WhatsApp/%'" +
                " AND " + MediaStore.Audio.Media.DATA + " NOT LIKE '%/Telegram/%'" +
                " AND " + MediaStore.Audio.Media.DATA + " NOT LIKE '%/Recordings/%'" +
                " AND " + MediaStore.Audio.Media.DATA + " NOT LIKE '%/Call Recordings/%'" +
                " AND " + MediaStore.Audio.Media.DATA + " NOT LIKE '%/Notifications/%'" +
                " AND " + MediaStore.Audio.Media.DATA + " NOT LIKE '%/Ringtones/%'" +
                " AND " + MediaStore.Audio.Media.DATA + " NOT LIKE '%/Alarms/%'" +
                " AND " + MediaStore.Audio.Media.DURATION + " > 30000"

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
        )

        val cursor = this.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            MediaStore.Audio.Media.DATE_ADDED + " DESC",
            null
        )

        cursor?.use {
            while (it.moveToNext()) {

                val id = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                val title = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val artist = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val duration = it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                val path = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                val albumId =
                    it.getLong(it.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()

                val uri = Uri.parse("content://media/external/audio/albumart")
                val arturi = Uri.withAppendedPath(uri, albumId).toString()

                val music = Music(id, title, artist, duration, path, arturi)
                tempMusicList.add(music)


            }
        }

        return tempMusicList
    }

    override fun onResume() {
        super.onResume()
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val editor = getSharedPreferences("Favourite", MODE_PRIVATE).edit()

        Favourites.FavMusicList = checkMusicExists(Favourites.FavMusicList)

        val jsonString = gson.toJson(Favourites.FavMusicList)

        Log.d("MoreOptions", "from main Saving FavMusicList: $jsonString")

        editor.putString("Fav_songs", jsonString)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (player.isPlaying == false && player.musicservice != null) {
            player.musicservice!!.stopForeground(STOP_FOREGROUND_REMOVE)
            player.musicservice!!.mediaPlayer!!.release()
            player.musicservice = null
            exitProcess(1)
        }

    }

    override fun onBackPressed() {
        if (mainBinding.drawerlayout.isDrawerOpen(GravityCompat.START)) {
            mainBinding.drawerlayout.closeDrawer(GravityCompat.START)
        } else {
            finishAffinity()
        }
    }

    fun ToastShower(dataToBeDisplayed: String) {
        Toast.makeText(applicationContext, dataToBeDisplayed, Toast.LENGTH_SHORT).show()
    }
}