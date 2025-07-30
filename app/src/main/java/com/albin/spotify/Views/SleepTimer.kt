package com.albin.spotify.Views

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.albin.spotify.R
import com.albin.spotify.databinding.ActivitySleepTimerBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class SleepTimer : AppCompatActivity() {

    lateinit var sleepBinding: ActivitySleepTimerBinding
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>


    //variables for the sleeper timer

    var FiveMin:Boolean=false
    var TenMin:Boolean=false
    var FifteenMin:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sleepBinding= ActivitySleepTimerBinding.inflate(layoutInflater)
        setContentView(sleepBinding.root)


        //setting up the bottomsheet

        val screenheight=resources.displayMetrics.heightPixels
        val halfScreenHeight =(screenheight * 0.70).toInt()

        bottomSheetBehavior= BottomSheetBehavior.from(sleepBinding.constraintLL)
        bottomSheetBehavior.state= BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.isHideable=true
        bottomSheetBehavior.isDraggable=true
        bottomSheetBehavior.peekHeight=halfScreenHeight


        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
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



    }
}