package com.dimaslanjaka.facebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dimaslanjaka.facebook.shared.Greeting
import android.widget.TextView
import com.dimaslanjaka.facebook.R

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "Android App"

        val tv: TextView = findViewById(R.id.text_view)
        tv.text = greet()
    }
}
