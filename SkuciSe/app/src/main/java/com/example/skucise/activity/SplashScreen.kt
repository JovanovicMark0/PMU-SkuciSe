package com.example.skucise.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.skucise.MainActivity
import com.example.skucise.R

class SplashScreen :AppCompatActivity(){

    lateinit var handler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        handler = Handler()
        handler.postDelayed({

            val intent = Intent( this, Session::class.java)
            startActivity(intent)
            finish()
        }, 1500)


    }

}