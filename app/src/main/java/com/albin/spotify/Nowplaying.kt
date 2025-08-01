package com.albin.spotify

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

class Nowplaying : Fragment() {


    companion object{
         var _binding: FragmentNowplayingBinding? = null
    }

    private val binding get() = _binding!!

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

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // Add null safety checks
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

    private fun playMusic() {
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
}