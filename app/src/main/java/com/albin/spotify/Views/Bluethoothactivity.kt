package com.albin.spotify.Views

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.albin.spotify.BTAdapter
import com.albin.spotify.BTPaired
import com.albin.spotify.R
import com.albin.spotify.databinding.ActivityBluethoothactivityBinding

class Bluethoothactivity : AppCompatActivity() {


   lateinit var bluebinding: ActivityBluethoothactivityBinding

   var deviceName:String?=null
    var deviceAddress:String?=null
   val REQUEST_ENABLE_BT=100
   var paired_DevicesList=ArrayList<BTPaired>()

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bluebinding= ActivityBluethoothactivityBinding.inflate(layoutInflater)
        setContentView(bluebinding.root)

        val bluthoothManager: BluetoothManager=getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter:BluetoothAdapter?=bluthoothManager.getAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(this@Bluethoothactivity,"Device dont support bluetooth",Toast.LENGTH_SHORT).show()
        }

        if (bluetoothAdapter!!.isEnabled==false)
        {
            val bIntent=Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(bIntent,REQUEST_ENABLE_BT)
        }

        bluebinding.searchBt.setOnClickListener {
            val pairedDevices:Set<BluetoothDevice> =bluetoothAdapter.bondedDevices
            pairedDevices.forEach {
                device->
                deviceName=device.name
                deviceAddress=device.address
                paired_DevicesList.add(BTPaired(deviceName,deviceAddress))
            }
        }

        val adapter= BTAdapter(this,paired_DevicesList)
        bluebinding.recyclerView.layoutManager= LinearLayoutManager(this)
        bluebinding.recyclerView.adapter=adapter

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==RESULT_OK && resultCode==REQUEST_ENABLE_BT)
        {
            return
        }
        else{
            Toast.makeText(this@Bluethoothactivity,"Device dont support bluetooth",Toast.LENGTH_SHORT).show()

        }
    }
}