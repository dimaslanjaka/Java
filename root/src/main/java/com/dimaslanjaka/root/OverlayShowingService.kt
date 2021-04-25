package com.dimaslanjaka.root

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import com.neptune.domino.Init
import kotlin.math.roundToInt


class OverlayShowingService : Service(), OnTouchListener, View.OnClickListener {
    private var topLeftView: View? = null
    private var overlayedButton: Button? = null
    lateinit var params: WindowManager.LayoutParams
    lateinit var mOverlayView: View
    var initialX = 0
    var initialY = 0
    var initialTouchX = 0f
    var initialTouchY = 0f

    private lateinit var wm: WindowManager
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()
        val LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        mOverlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null);
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.LEFT
        params.x = 0
        params.y = 100

        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        wm.addView(mOverlayView, params)

        val btnInject = mOverlayView.findViewById<Button>(R.id.btnFloat)
        btnInject.setOnTouchListener(this)

        val selector = mOverlayView.findViewById<Spinner>(R.id.spinner1)
        selector.setOnTouchListener(this)

        val wrapper = mOverlayView.findViewById<LinearLayout>(R.id.wrapper)
        wrapper.setOnTouchListener(this)

        instance = this
    }

    override fun onDestroy() {
        super.onDestroy()
        if (overlayedButton != null) {
            wm.removeView(overlayedButton)
            wm.removeView(topLeftView)
            overlayedButton = null
            topLeftView = null
        }
    }

    private fun isAClick(startX: Float, endX: Float, startY: Float, endY: Float): Boolean {
        val CLICK_ACTION_THRESHOLD = 200
        val differenceX = Math.abs(startX - endX)
        val differenceY = Math.abs(startY - endY)
        return !(differenceX > CLICK_ACTION_THRESHOLD /* =5 */ || differenceY > CLICK_ACTION_THRESHOLD)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //remember the initial position.
                initialX = params.x
                initialY = params.y
                //get the touch location
                initialTouchX = event.rawX
                initialTouchY = event.rawY
                return true
            }
            MotionEvent.ACTION_UP -> {
                //Add code for launching application and positioning the widget to nearest edge.
                val endX = event.x;
                val endY = event.y;
                if (isAClick(initialX.toFloat(), endX, initialY.toFloat(), endY)) {
                    //Toast().make(applicationContext, "starting tweak")
                    //Init.start(this)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Init(applicationContext)
                    }
                    Log.d("service", "button clicked")
                }
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val Xdiff = (event.rawX - initialTouchX).roundToInt().toFloat()
                val Ydiff = (event.rawY - initialTouchY).roundToInt().toFloat()


                //Calculate the X and Y coordinates of the view.
                params.x = initialX + Xdiff.toInt()
                params.y = initialY + Ydiff.toInt()

                //Update the layout with new X & Y coordinates
                wm.updateViewLayout(mOverlayView, params)
                return true
            }
        }
        return false
    }

    override fun onClick(v: View) {
        Toast.makeText(this, "Overlay button click event", Toast.LENGTH_SHORT).show()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: OverlayShowingService
        fun stopService() {
            instance.stopSelf()
        }
    }
}