package com.ew.jsontree

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.HorizontalScrollView

class MainActivity : Activity() {
    private val llRoot: HorizontalScrollView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn1 = findViewById<Button>(R.id.btn1)
        val btn2 = findViewById<Button>(R.id.btn2)
        val filepicker = findViewById<EditText>(R.id.filePicker)
        val fileView = findViewById<Button>(R.id.viewFile)

        fileView.setOnClickListener {
            val intent = Intent()
            intent.setClass(this@MainActivity, CustomJson::class.java)
            startActivity(intent)
        }

        btn1.setOnClickListener {
            val intent = Intent()
            intent.setClass(this@MainActivity, JsonTreeActivity::class.java)
            startActivity(intent)
        }
        btn2.setOnClickListener {
            val intent = Intent()
            intent.setClass(this@MainActivity, AdvancedJsonTreeActivity::class.java)
            startActivity(intent)
        }
    }
}