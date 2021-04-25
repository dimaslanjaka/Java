package com.dimaslanjaka.root

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.neptune.domino.Init
import kotlinx.android.synthetic.main.simple_activity.*


class SimpleActivity : AppCompatActivity() {
    val DRAW_OVER_OTHER_APP_PERMISSION = 123;
    val memoryBoss = MemoryBoss()
    lateinit var intentService: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.simple_activity)
        registerComponentCallbacks(memoryBoss)

        askForSystemOverlayPermission()

        intentService = Intent(
            this@SimpleActivity,
            OverlayShowingService::class.java
        ).putExtra("activity_background", true)
        buttonStart.setOnClickListener { startFloating() }
        buttonStop.setOnClickListener {
            stopFloating()
            stopService(intentService)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity()
            }
        }
        manual.setOnClickListener {
            if (isStoragePermissionGranted()) Init.start(this)
        }

        if (isStoragePermissionGranted())
            if (Core.debug) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Init(this)
                }
            }
    }

    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v("storage", "Permission is granted")
                true
            } else {
                Log.v("storage", "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("storage", "Permission is granted")
            true
        }
    }

    override fun onPause() {
        super.onPause()
        Core.isAppInBackground = true
    }

    override fun onResume() {
        super.onResume()
        Core.isAppInBackground = false
    }

    fun startFloating() {
        if (Core.isAppInBackground) {
            Log.e("floating", "app in background")
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Log.e("floating", "app doesnt have over in app permission")
                return
            }
        }
        startService(intentService)
    }

    fun stopFloating() {
        stopService(intentService)
        OverlayShowingService.stopService()
    }

    private fun askForSystemOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DRAW_OVER_OTHER_APP_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    //Permission is not available. Display error text.
                    errorToast()
                    finish()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun errorToast() {
        Toast.makeText(
            this,
            "Draw over other app permission not available. Can't start the application without the permission.",
            Toast.LENGTH_LONG
        ).show()
    }
}