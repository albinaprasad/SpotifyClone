package com.albin.spotify

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.albin.spotify.Views.player
import com.albin.spotify.Views.player.Companion.PlayermusicList
import com.albin.spotify.Views.player.Companion.isPlaying
import com.albin.spotify.Views.player.Companion.musicservice
import com.albin.spotify.Views.player.Companion.playerBinding
import com.albin.spotify.Views.player.Companion.position
import com.albin.spotify.databinding.FragmentNowplayingBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import android.view.MotionEvent
import android.widget.Toast
import com.albin.spotify.Views.player.Companion.isRepeat
import androidx.palette.graphics.Palette
import android.graphics.Bitmap
import androidx.core.graphics.ColorUtils
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition


class Nowplaying : Fragment() {

    var startX=0f

    companion object{
         var _binding: FragmentNowplayingBinding? = null
    }

    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNowplayingBinding.inflate(inflater, container, false)

        // Initialize the fragment as invisible initially
        binding.root.visibility = View.INVISIBLE

        // Set up click listeners
        binding.miniPlayerPlayPause.setOnClickListener {
            if (player.isPlaying) {
                pauseMusic()
            } else {
                playMusic()
            }
        }

        //swipe gester for music change

     binding.root.setOnTouchListener {view,event->

         when(event.action)
         {
             MotionEvent.ACTION_DOWN -> {
                 startX = event.x
                 true
             }

             MotionEvent.ACTION_UP->{

                 var endX=event.x

                 val differenceX=startX-endX

                 val minSwipeDistance = 100f

                 if (kotlin.math.abs(differenceX) > minSwipeDistance) {

                     if (differenceX > 0) {

                         playNextSong()
                     } else {

                         playPreviousSong()
                     }
                 }
                 true
             }

             else -> false

             }
         }
//


        return binding.root
    }



    override fun onResume() {
        super.onResume()

        // Add null safety checks
        setNowplayingLayout()

    }

    private fun setNowplayingLayout() {




        if (player.musicservice?.mediaPlayer != null &&
            PlayermusicList.isNotEmpty() &&
            position < PlayermusicList.size) {

            binding.root.visibility = View.VISIBLE

            // Load album art with Glide
            Glide.with(this)
                .load(PlayermusicList[position].imageuri)
                .apply(RequestOptions.placeholderOf(R.drawable.musical_icon))
                .centerCrop()
                .into(binding.miniPlayerImage)

            // Enable marquee scrolling
            binding.songNameFrag.isSelected = true
            binding.artistFrag.isSelected = true

            // Set song details
            binding.songNameFrag.text = PlayermusicList[position].title
            binding.artistFrag.text = PlayermusicList[position].singer

            // Update play/pause button based on current state
            updatePlayPauseButton()

            musicservice!!. mediaPlayer?.let {
                it.stop()
                it.reset()
                it.release()
            }
            musicservice!!. mediaPlayer = null

            musicservice!!.mediaPlayer= MediaPlayer()
            musicservice!!. mediaPlayer!!.reset()
            musicservice!!.mediaPlayer!!.setDataSource(PlayermusicList[position].path)
            musicservice!!.mediaPlayer!!.prepare()
            musicservice!!.mediaPlayer!!.start()


            setdynamicBackgroundColor()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updatePlayPauseButton() {
        if (player.isPlaying) {
            binding.miniPlayerPlayPause.setImageResource(R.drawable.pause)
        } else {
            binding.miniPlayerPlayPause.setImageResource(R.drawable.playandpause)
        }
    }

    private fun playMusic(){
        try {
            binding.miniPlayerPlayPause.setImageResource(R.drawable.pause)

            musicservice?.mediaPlayer?.start()
            player.musicservice?.showNotification()
            player.isPlaying = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun pauseMusic() {
        try {
            binding.miniPlayerPlayPause.setImageResource(R.drawable.playandpause)

            // Update main player button if available


            musicservice?.mediaPlayer?.pause()
            player.musicservice?.showNotification()
            player.isPlaying = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun playNextSong()
    {
       // Toast.makeText(requireActivity(),"next", Toast.LENGTH_SHORT).show()
        if(isRepeat== false) {

            if (position == PlayermusicList.size - 1) {
                position = 0
            } else {
                position++
            }
            setNowplayingLayout()
            player.musicservice!!.showNotification()
        }

    }
    private fun playPreviousSong() {
       /// Toast.makeText(requireActivity(),"previosu", Toast.LENGTH_SHORT).show()

        if (position == 0 )
        {
            position=PlayermusicList.size-1

        }
        else
        {
            position--
        }
        setNowplayingLayout()
        player.musicservice!!.showNotification()
    }

   fun  setdynamicBackgroundColor()
    {
        var dynamic_imageUri=PlayermusicList[position].imageuri.toString()

        Glide.with(this)
            .asBitmap()
            .load(dynamic_imageUri)
            .into(object:CustomTarget<Bitmap>(){
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {

                    Palette.from(resource).generate{ palette->
                    palette?.let{

                        val dominantColor=it.getDominantColor(Color.parseColor("#2C2C2C"))
                        applyBackgroundColor(dominantColor)
                        }
                    }
                }


                override fun onLoadCleared(placeholder: Drawable?) {
                    binding.root.setBackgroundColor(Color.parseColor("#2C2C2C"))
                }
            })
    }
    private fun applyBackgroundColor(dominantColor: Int) {

        val radius=18f
        val roundedBackground = GradientDrawable()
        roundedBackground.shape = GradientDrawable.RECTANGLE
        roundedBackground.cornerRadius = radius

        val backgroundTint = ColorUtils.blendARGB(
            dominantColor,      // The color from album art
            Color.BLACK,        // Mix with black to keep it dark
            0.30f
        )
        val finalBackground = ColorUtils.setAlphaComponent(backgroundTint, 230)
        //binding.root.setBackgroundColor(finalBackground)

        roundedBackground.setColor(finalBackground)
        binding.root.background=roundedBackground
    }

}