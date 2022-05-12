package com.example.skucise.activity

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.example.skucise.HomePage
import com.example.skucise.MainActivity


class Session : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val Registered: Boolean
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        Registered = sharedPref.getBoolean("Registered", false)

        if (!Registered) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, HomePage::class.java))
        }
    }
}