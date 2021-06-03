package com.dimaslanjaka.browser

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import android.Activity.start as startOtherActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        preferences = PreferenceManager.getDefaultSharedPreferences(this)

        val startbrowser = findViewById<Button>(R.id.startbrowser)
        startbrowser.setOnClickListener {
            val extras = mutableMapOf<String, String>()
            preferences.getString("filename", "default")?.let { cookie_filename ->
                extras["COOKIE_FILENAME"] = cookie_filename
            }
            startOtherActivity(MainActivity::class.java, extras)
        }

        val clearcookie = findViewById<Button>(R.id.clearcookie)
        clearcookie.setOnClickListener {

        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

    companion object {
        lateinit var preferences: SharedPreferences
    }
}