package com.albin.spotify.Views

import android.app.Service
import android.app.Service.STOP_FOREGROUND_REMOVE
import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.albin.spotify.R
import com.albin.spotify.Views.player.Companion.playerBinding
import com.albin.spotify.databinding.ActivitySleepTimerBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import java.sql.Time

class SleepTimer : AppCompatActivity() {

    lateinit var sleepBinding: ActivitySleepTimerBinding
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    var currentTimerName = "none"
    var runnable: Runnable? = null
    lateinit var sharedpreference: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sharedpreference = getSharedPreferences("sleepTimer", Context.MODE_PRIVATE)


        sleepBinding = ActivitySleepTimerBinding.inflate(layoutInflater)
        setContentView(sleepBinding.root)


        //restore data
        restoreData()


        //five min button
        sleepBinding.fiveMin.setOnClickListener {

            setTimer("five_min", 5 * 60 * 1000L, sleepBinding.fiveMin)
        }

        //10 min
        sleepBinding.TenMin.setOnClickListener {
            setTimer("ten_min", 10 * 60 * 1000L, sleepBinding.TenMin)
        }
        //15 min

        sleepBinding.FifteenMin.setOnClickListener {
            setTimer("fifteen_min", 15 * 60 * 1000L, sleepBinding.FifteenMin)
        }

        //30 min
        sleepBinding.ThirtyMin.setOnClickListener {
            setTimer("thirty_min", 30 * 60 * 1000L, sleepBinding.ThirtyMin)
        }

        //45min
        sleepBinding.FifteenMin.setOnClickListener {
            setTimer("forty_five_min", 45 * 60 * 1000L, sleepBinding.FifteenMin)
        }

        //1hour
        sleepBinding.oneHr.setOnClickListener {
            setTimer("one_hour", 60 * 60 * 1000L, sleepBinding.oneHr)
        }

        //end of track

        sleepBinding.EndOfTrakBtn.setOnClickListener {

            val duration = intent.getLongExtra("duration", 0L)
            val currentpostion = player.musicservice!!.mediaPlayer!!.currentPosition

            var timeDiff = duration - currentpostion
            if (timeDiff > 0) {
                setTimer("end_of_track", timeDiff, sleepBinding.EndOfTrakBtn)
            } else {
                Toast.makeText(applicationContext, "Cant set timer", Toast.LENGTH_SHORT).show()
            }

            Log.d("Time", duration.toString())
            Log.d("Time", currentpostion.toString())

        }

        //setting up the bottomsheet

        val screenheight = resources.displayMetrics.heightPixels
        val halfScreenHeight = (screenheight * 0.70).toInt()

        bottomSheetBehavior = BottomSheetBehavior.from(sleepBinding.constraintLL)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.isDraggable = true
        bottomSheetBehavior.peekHeight = halfScreenHeight


        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        finish()
                    }
                }

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    private fun restoreData() {

        val savedTimerName = sharedpreference.getString("timerName", "none") ?: "none"
        val savedTimeMillis = sharedpreference.getLong("timerMillis", 0L)
        val greenColor = ContextCompat.getColor(this, R.color.green)

        if (savedTimerName == "none") return

        currentTimerName = savedTimerName
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - sharedpreference.getLong("timerStartTime", currentTime)
        val remainingTime = savedTimeMillis - elapsedTime

        if (remainingTime > 0) {
            val buttonToColor = when (savedTimerName) {
                "five_min" -> sleepBinding.fiveMin
                "ten_min" -> sleepBinding.TenMin
                "fifteen_min" -> sleepBinding.FifteenMin
                "thirty_min" -> sleepBinding.ThirtyMin
                "forty_five_min" -> sleepBinding.FortyFiveMin
                "one_hour" -> sleepBinding.oneHr
                "end_of_track" -> sleepBinding.EndOfTrakBtn
                else -> null
            }
            resetAllButton()
            buttonToColor?.setTextColor(ColorStateList.valueOf(greenColor))
            playerBinding.timerBtn.setColorFilter(greenColor, PorterDuff.Mode.SRC_IN)

            runnable = Runnable {
                if (currentTimerName != "none" && player.musicservice != null) {
                    player.musicservice?.stopForeground(STOP_FOREGROUND_REMOVE)
                    player.musicservice = null
                    sharedpreference.edit().clear().apply()
                    currentTimerName = "none"
                    resetAllButton()
                    playerBinding.timerBtn.clearColorFilter()
                    finishAffinity()
                }
            }
            Handler(Looper.getMainLooper()).postDelayed(runnable!!, remainingTime)
        } else {
            sharedpreference.edit().clear().apply()
            currentTimerName = "none"
        }
    }


    fun resetAllButton() {
        val whiteColor = ContextCompat.getColor(this, R.color.white)

        sleepBinding.fiveMin.setTextColor(ColorStateList.valueOf(whiteColor))
        sleepBinding.TenMin.setTextColor(ColorStateList.valueOf(whiteColor))
        sleepBinding.FifteenMin.setTextColor(ColorStateList.valueOf(whiteColor))
        sleepBinding.ThirtyMin.setTextColor(ColorStateList.valueOf(whiteColor))
        sleepBinding.FortyFiveMin.setTextColor(ColorStateList.valueOf(whiteColor))
        sleepBinding.EndOfTrakBtn.setTextColor(ColorStateList.valueOf(whiteColor))
        playerBinding.timerBtn.setColorFilter(whiteColor, PorterDuff.Mode.SRC_IN)
    }

    fun setTimer(timerName: String, TimePeriod: Long, button: MaterialButton) {
        val greenColor = ContextCompat.getColor(this, R.color.green)

        if (timerName == currentTimerName) {
            if (runnable != null) {
                Handler(Looper.getMainLooper()).removeCallbacks(runnable!!)
                runnable = null
                currentTimerName = "none"
            }
            resetAllButton()
            sharedpreference.edit().clear().apply()
        } else {
            if (runnable != null) {
                Handler(Looper.getMainLooper()).removeCallbacks(runnable!!)
                runnable = null
                currentTimerName = "none"
            }
            resetAllButton()
            button.setTextColor(ColorStateList.valueOf(greenColor))
            currentTimerName = timerName

            playerBinding.timerBtn.setColorFilter(greenColor, PorterDuff.Mode.SRC_IN)

            sharedpreference.edit().apply() {
                putString("timerName", timerName)
                putLong("timerMillis", TimePeriod)
                putLong("timerStartTime", System.currentTimeMillis())
                apply()

            }
            runnable = Runnable {
                if (currentTimerName != "none" && player.musicservice != null) {
                    player.musicservice?.stopForeground(STOP_FOREGROUND_REMOVE)
                    player.musicservice = null
                    finishAffinity()
                }

            }
            Handler(Looper.getMainLooper()).postDelayed(runnable!!, TimePeriod)
        }

    }
}



