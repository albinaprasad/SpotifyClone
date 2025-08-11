package com.albin.spotify.Views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.albin.spotify.MainActivity
import com.albin.spotify.PlayListAdapter
import com.albin.spotify.R
import com.albin.spotify.databinding.ActivityPlaylistsBinding

class Playlists : AppCompatActivity() {

    lateinit var playBinding: ActivityPlaylistsBinding
    companion object{
        lateinit var playlistadapter: PlayListAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        playBinding= ActivityPlaylistsBinding.inflate(layoutInflater)
        setContentView(playBinding.root)



        playBinding.profileBtn.setOnClickListener {
            playBinding.drawerlayout.openDrawer(GravityCompat.START)
        }
        playBinding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_addprofile -> {
                    Toast.makeText(applicationContext, "Add account", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_whatsnew -> {
                    Toast.makeText(applicationContext, "What's new clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_recents -> {
                    Toast.makeText(applicationContext, "Recents clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_updates -> {
                    Toast.makeText(applicationContext, "Updates clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_settings -> {
                    Toast.makeText(applicationContext, "Settings clicked", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_exit -> {
                    finish() // Add actual exit functionality
                }
            }
            playBinding.drawerlayout.closeDrawer(GravityCompat.START)
            true
        }
        //animation set up for nav bar
        playBinding.drawerlayout.addDrawerListener(object: DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                var movedistance = drawerView.width*slideOffset
                playBinding.main.translationX=movedistance
            }

            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerClosed(drawerView: View) {

            }

            override fun onDrawerStateChanged(newState: Int) {

            }
        })


        //adapter setup

        playBinding.playListRV.layoutManager = GridLayoutManager(this@Playlists,2)
         playlistadapter = PlayListAdapter(this, createPlaylist.musicPlaylitObj.ref)
        playBinding.playListRV.adapter = playlistadapter


        //create new playlists

        playBinding.addPlaylist.setOnClickListener {
            val addplayListintent= Intent(this@Playlists, createPlaylist::class.java)
            startActivity(addplayListintent)
        }

    }

    override fun onBackPressed()
    {
        if (playBinding.drawerlayout.isDrawerOpen(GravityCompat.START)) {
            playBinding.drawerlayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    override fun onResume() {
        super.onResume()
        playlistadapter.refreshPlaylistData()
    }
}