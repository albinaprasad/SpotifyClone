package com.albin.spotify.Views

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.albin.spotify.BTAdapter
import com.albin.spotify.BTPaired
import com.albin.spotify.R
import com.albin.spotify.databinding.ActivityBluethoothactivityBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlin.text.clear

class Bluethoothactivity : AppCompatActivity() {


    lateinit var bluebinding: ActivityBluethoothactivityBinding
    lateinit var bottomSheet: BottomSheetBehavior<View>

    var deviceName: String? = null
    var deviceAddress: String? = null
    val REQUEST_ENABLE_BT = 100
    var paired_DevicesList = ArrayList<BTPaired>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bluebinding = ActivityBluethoothactivityBinding.inflate(layoutInflater)
        setContentView(bluebinding.root)

        val bluthoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluthoothManager.getAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(
                this@Bluethoothactivity,
                "Device dont support bluetooth",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (bluetoothAdapter!!.isEnabled == false) {
            val bIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(bIntent, REQUEST_ENABLE_BT)
        }

        bluebinding.searchBt.setOnClickListener {


            if (checkBluetoothPermissions()) {
                loadPairedDevices(bluetoothAdapter)
            } else {
                requestBluetoothPermissions()
            }


        }

        val adapter = BTAdapter(this, paired_DevicesList)
        bluebinding.recyclerView.layoutManager = LinearLayoutManager(this)
        bluebinding.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        //bottom shett
        val screenheight = resources.displayMetrics.heightPixels
        val halfScreenHeight = (screenheight * 0.70).toInt()

        bottomSheet = BottomSheetBehavior.from(bluebinding.Linearmain)
        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheet.isHideable = true
        bottomSheet.isDraggable = true
        bottomSheet.peekHeight = halfScreenHeight

        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            return
        } else {
            Toast.makeText(
                this@Bluethoothactivity,
                "Device dont support bluetooth",
                Toast.LENGTH_SHORT
            ).show()

        }
    }

    private fun loadPairedDevices(bluetoothAdapter: BluetoothAdapter) {
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
        paired_DevicesList.clear()
        pairedDevices?.forEach { device ->
            val deviceName = device.name ?: "Unknown Device"
            val deviceAddress = device.address ?: "Unknown Address"
            paired_DevicesList.add(BTPaired(deviceName, deviceAddress))
        }
        bluebinding.recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun checkBluetoothPermissions(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_ADMIN
                    ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestBluetoothPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                200
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN),
                200
            )
        }
    }
}




