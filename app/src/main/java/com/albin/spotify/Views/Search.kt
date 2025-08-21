package com.albin.spotify.Views

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.albin.spotify.Carditems
import com.albin.spotify.MainActivity
import com.albin.spotify.R
import com.albin.spotify.Recents
import com.albin.spotify.databinding.ActivitySearchBinding
import com.albin.spotify.searchAdaptertwo
import com.albin.spotify.serachCardAdapter

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.material.card.MaterialCardView

class Search : AppCompatActivity() {
    var carditems = ArrayList<Carditems>()
    var carditemsTwo= ArrayList<Carditems>()
    lateinit var searchBinding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        searchBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchBinding.root)

//first 4 card view setup
        carditems.add(Carditems("Muisc", R.color.pink, R.drawable.singer))
        carditems.add(Carditems("Podcasts", R.color.Darkgreen, R.drawable.science))
        carditems.add(Carditems("Live Events", R.color.violet, R.drawable.singere_male))
        carditems.add(Carditems("Home of\nI-Pop", R.color.Dblue, R.drawable.green_symbol))

        searchBinding.recyclerView2.isNestedScrollingEnabled = false
        searchBinding.recyclerView1.isNestedScrollingEnabled = false

        val adapterOne= serachCardAdapter(this@Search,carditems)
        searchBinding.recyclerView1.layoutManager= GridLayoutManager(this,2)
        searchBinding.recyclerView1.adapter=adapterOne

//vide view set up



        videoSetup()

        //adapter setup for the next card in the views

        setupcardItems()

        val adapterTwo= searchAdaptertwo(this@Search,carditemsTwo)
        searchBinding.recyclerView2.layoutManager= GridLayoutManager(this,2)
        searchBinding.recyclerView2.adapter=adapterTwo

//sticky header setup
        setStickyHeader()

        //setup animatoion for the videoplayer

        videAnimation( searchBinding.card1)
        videAnimation( searchBinding.card2)
        videAnimation( searchBinding.card3)

//camera btn setup
searchBinding.camera.setOnClickListener {

    val cintent=Intent(this@Search, cameraActivity::class.java)
    startActivity(cintent)
}


//nav bar
        searchBinding.navButton.setOnClickListener {
            searchBinding.drawerlayout.openDrawer(GravityCompat.START)
        }

        searchBinding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_addprofile -> {
                    ToastShower("Add account")
                }

                R.id.nav_whatsnew -> {
                    ToastShower("What's new clicked")
                }

                R.id.nav_recents -> {

                    val recentIntent=Intent(this@Search, Recents::class.java)
                    startActivity(recentIntent)
                    //ToastShower("Recents clicked")
                }

                R.id.nav_updates -> {
                    ToastShower("Updates clicked")
                }

                R.id.nav_search->{
                    val serachIntent=Intent(this@Search, Search::class.java)
                    startActivity(serachIntent)
                }

                R.id.nav_settings -> {
                    ToastShower("Settings clicked")
                }

                R.id.nav_exit -> {
                    finish() // Add actual exit functionality
                }
            }
            searchBinding.drawerlayout.closeDrawer(GravityCompat.START)
            true
        }

        navbarAnimationSetUp()
    }

    fun navbarAnimationSetUp() {

        searchBinding.drawerlayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                var moveDistance = drawerView.width * slideOffset
                searchBinding.main.translationX = moveDistance
            }

            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerClosed(drawerView: View) {

            }

            override fun onDrawerStateChanged(newState: Int) {

            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun videAnimation(card: MaterialCardView) {
       card.setOnTouchListener { view,event->

            when(event.action){

                MotionEvent.ACTION_DOWN->{
                    view.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).start()

                }
                MotionEvent.ACTION_CANCEL,MotionEvent.ACTION_UP->{
                    view.animate().scaleX(1f).scaleY(1f).setDuration(100).start()

                }

            }
            true
        }
    }

    fun setStickyHeader(){

        val scrollView = searchBinding.main
        var searchBr= findViewById<com.google.android.material.search.SearchBar>(R.id.search_bar)
        scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->

            if (scrollY > searchBr.top) {
                // User has scrolled past SearchBar - make it stick!
                searchBr.translationY = (scrollY - searchBr.top).toFloat()
                searchBr.elevation = 8f
                window.decorView.systemUiVisibility = (
                        SYSTEM_UI_FLAG_FULLSCREEN or
                                SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                                SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        )
            } else {
                // User is back at top - make it normal
                searchBr.translationY = 0f
                searchBr.elevation = 0f  // Remove shadow
            }
        }

    }

    private fun setupcardItems( ) {

        carditemsTwo.add(Carditems("Made\nFor You", R.color.PURPLE, R.drawable.singer))
        carditemsTwo.add(Carditems("Upcom-\ning Rel..", R.color.Darkgreen, R.drawable.green_symbol))
        carditemsTwo.add(Carditems("Rain&\nMono..", R.color.Darkgreen, R.drawable.women_dancing))
        carditemsTwo.add(Carditems(" New\nReleases", R.color.lGREEN, R.drawable.singere_male))

        carditemsTwo.add(Carditems("Hindi", R.color.pink, R.drawable.img_2))
        carditemsTwo.add(Carditems("Telugu", R.color.ORANGE, R.drawable.pushpa))
        carditemsTwo.add(Carditems("Punjabi", R.color.PUNJABIPURPLE, R.drawable.img_2))
        carditemsTwo.add(Carditems("charts", R.color.PURPLE, R.drawable.women_dancing))

        carditemsTwo.add(Carditems("Podcast\nCharts", R.color.Dblue, R.drawable.f_one))
        carditemsTwo.add(Carditems("Podcast\nNew R..", R.color.PURPLE, R.drawable.fafa))
        carditemsTwo.add(Carditems("Video\nPodcas..", R.color.RED, R.drawable.img_3))
        carditemsTwo.add(Carditems("Tamil", R.color.ORANGE, R.drawable.rajini))

        carditemsTwo.add(Carditems("Workout", R.color.ASH, R.drawable.workout))
        carditemsTwo.add(Carditems("Indian\nClassical", R.color.SANDEL, R.drawable.indian_classical))
        carditemsTwo.add(Carditems("Love", R.color.PUNJABIPURPLE, R.drawable.equal))
        carditemsTwo.add(Carditems("Business", R.color.ASH, R.drawable.party))


        carditemsTwo.add(Carditems("Malaya\nlam", R.color.LBLUE, R.drawable.fafa))
        carditemsTwo.add(Carditems("Summer", R.color.lGREEN, R.drawable.ice_cream))
        carditemsTwo.add(Carditems("R&B", R.color.BROWN, R.drawable.f_one))
        carditemsTwo.add(Carditems("Focus", R.color.SANDEL, R.drawable.orange_sky))

    }

    fun ToastShower(dataToBeDisplayed: String) {
        Toast.makeText(applicationContext, dataToBeDisplayed, Toast.LENGTH_SHORT).show()
    }

    private fun videoSetup() {
        val player1 = ExoPlayer.Builder(this).build().apply {
            val videoUri = Uri.parse("android.resource://$packageName/${R.raw.vid_five}")
            setMediaItem(MediaItem.fromUri(videoUri))
            playWhenReady = true
            repeatMode = ExoPlayer.REPEAT_MODE_ONE
            volume = 0f
            prepare()
        }
        searchBinding.video1.player = player1

// Video 2
        val player2 = ExoPlayer.Builder(this).build().apply {
            val videoUri = Uri.parse("android.resource://$packageName/${R.raw.video_one}")
            setMediaItem(MediaItem.fromUri(videoUri))
            playWhenReady = true
            repeatMode = ExoPlayer.REPEAT_MODE_ONE
            volume = 0f
            prepare()
        }
        searchBinding.video2.player = player2

// Video 3
        val player3 = ExoPlayer.Builder(this).build().apply {
            val videoUri = Uri.parse("android.resource://$packageName/${R.raw.vid_four}")
            setMediaItem(MediaItem.fromUri(videoUri))
            playWhenReady = true
            repeatMode = ExoPlayer.REPEAT_MODE_ONE
            volume = 0f
            prepare()
        }
        searchBinding.video3.player = player3
    }
}