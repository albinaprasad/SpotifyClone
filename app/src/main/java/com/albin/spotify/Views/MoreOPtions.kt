package com.albin.spotify.Views

import android.app.ComponentCaller
import android.content.Intent
import android.media.audiofx.AudioEffect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.albin.spotify.MainActivity
import com.albin.spotify.Music
import com.albin.spotify.R
import com.albin.spotify.Views.player.Companion.PlayermusicList
import com.albin.spotify.Views.player.Companion.playerBinding
import com.albin.spotify.Views.player.Companion.position
import com.albin.spotify.databinding.ActivityMoreOptionsBinding
import com.albin.spotify.favSongFind
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class MoreOPtions : AppCompatActivity() {

    companion object{
        var isFav: Boolean=false
        var favIndex=-1
        var songList= ArrayList<Music>()
    }

    lateinit var moreOptionsBinding: ActivityMoreOptionsBinding
    lateinit var bottomSheet: BottomSheetBehavior<View>

    val REQUESTCODE=8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        moreOptionsBinding= ActivityMoreOptionsBinding.inflate(layoutInflater)
        setContentView(moreOptionsBinding.root)


        //setup the bottom sheet
        setupBottomSheet()
        setImageAndTitle()


        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState)
                {
                    BottomSheetBehavior.STATE_HIDDEN->{
                        finish()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })




        //equaliser
        moreOptionsBinding.EqualiserMO.setOnClickListener {

            try {
                val EqIntent= Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
                EqIntent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, player.musicservice!!.mediaPlayer!!.audioSessionId)
                EqIntent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME,baseContext.packageName)
                EqIntent.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
                startActivityForResult(EqIntent,REQUESTCODE)
            }
            catch (e: Exception){
                Toast.makeText(this,"Equaliser error", Toast.LENGTH_SHORT).show()
                Log.d("Equaliser","Error in Equiliser")
            }
        }

//favourite button
        moreOptionsBinding.favMO.setOnClickListener {
            var currentpos=intent.getIntExtra("position",0)

            if( isFav )
            {
                isFav=false
                moreOptionsBinding.favMO.setIconResource(R.drawable.favourite)

                var FavPos=favSongFind(songList[currentpos].id)

                Log.d("fav",FavPos.toString())

                if(FavPos != -1)
                {
                    Favourites.FavMusicList.removeAt(FavPos)
                }
            }
            else{
                isFav=true
                moreOptionsBinding.favMO.setIconResource(R.drawable.full_love)
                Favourites.FavMusicList.add(songList[currentpos])
            }

        }


    }

    private fun setImageAndTitle() {

        songList= MainActivity.musicList


        var position = intent.getIntExtra("position",0)
        var artist:String = songList[position].singer
        var songName: String=songList[position].title

        Glide.with(this).load(songList[position].imageuri).apply(RequestOptions.placeholderOf(R.drawable.musical_icon))
            .centerCrop().into(moreOptionsBinding.albumArtMO)

        moreOptionsBinding.albumNameMO.text=songName
        moreOptionsBinding.albumNameMO.setSelected(true)
        moreOptionsBinding.artitNameMO.text=artist
        moreOptionsBinding.artitNameMO.setSelected(true)

        favIndex=favSongFind(songList[position].id)

        if( favIndex == -1 )
        {
            isFav=false
            moreOptionsBinding.favMO.setIconResource(R.drawable.favourite)
        }
        else
        {
            isFav=true
            moreOptionsBinding.favMO.setIconResource(R.drawable.full_love)
        }


    }

    private fun setupBottomSheet() {

        val screenHeight = resources.displayMetrics.heightPixels
        val halfScreenHeight =(screenHeight * 0.70).toInt()

        bottomSheet = BottomSheetBehavior.from(moreOptionsBinding.main)
        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheet.isDraggable=true
        bottomSheet.peekHeight = halfScreenHeight
        bottomSheet.isHideable=true
    }



    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if(REQUESTCODE==resultCode &&  resultCode==RESULT_OK)
        {
            return
        }
    }
}