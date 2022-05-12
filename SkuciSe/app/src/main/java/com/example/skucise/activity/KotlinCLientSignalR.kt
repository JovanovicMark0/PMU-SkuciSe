package com.example.skucise.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import com.example.skucise.R
import com.google.gson.JsonPrimitive

class KotlinCLientSignalR : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_client_signal_r)

/*
        val webView = findViewById<WebView>(R.id.webView)
        val connection = SignalR(this, "http://localhost:5000", SignalR.ConnectionType.HUB, webView)
        val hub = Hub("hub")
        hub.on("message") { args ->
            val name = (args?.get(0) as JsonPrimitive).asString
            val message = (args.get(1) as JsonPrimitive).asString
            Log.d("AndroidSignalR", "${name}: ${message}")
        }
        connection.addHub(hub)
        connection.start()
*/
    }
}