package com.example.skucise.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingwithmitch.kotlinrecyclerviewexample.NotificationsRecyclerAdapter
import com.example.skucise.R
import com.example.skucise.fragments.Notifications.Companion.notificationAdapter
import com.example.skucise.fragments.Notifications.Companion.notifikacije
import com.example.skucise.model.Messages
import com.example.skucise.model.Obavestenje
import com.example.skucise.util.Constants
import kotlinx.android.synthetic.main.aactivity_main.view.*
import kotlinx.android.synthetic.main.aactivity_main.view.recycler_view
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notifications.view.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime

class Notifications : Fragment() {

    companion object{
        lateinit var notificationAdapter: NotificationsRecyclerAdapter
        lateinit var viewPom : View
        var notifikacije = mutableListOf<Obavestenje>()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view : View = inflater!!.inflate(R.layout.fragment_notifications, container, false)
        viewPom = view



        val sharedPref = PreferenceManager.getDefaultSharedPreferences(view.context)
        val usernameLogovanog = sharedPref.getString("Username", " ")
        var res = StringBuffer()
        res = sendGetRequest("svaObavestenjaKorisnika/" + usernameLogovanog)

        return view
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun  sendGetRequest(requestText :String) :StringBuffer{

        val mURL = URL(Constants.BASE_URL + "api/"+requestText)
        val response = StringBuffer()
        with(mURL.openConnection() as HttpURLConnection) {

            requestMethod = "GET"

            var obj = JSONArray()
            println("URL : $url")
            println("Response Code : $responseCode")
            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()
                notifikacije.clear()
                while (inputLine != null) {
                    response.append(inputLine)
                    obj = JSONArray(inputLine)


                    for (i in 0 until obj.length()) {
                        val book = obj.getJSONObject(i)


                        var notifikacija = Obavestenje(book.get("idObavestenja").toString(),book.get("idOglasa").toString(),book.get("idPosiljaoca").toString(),book.get("idPrimaoca").toString(),book.get("datum").toString(),book.get("prihvacen").toString().toInt(),book.get("usernamePosiljaoca").toString(), book.get("naslovOglasa").toString())
                        notifikacije.add(notifikacija)
                        println(notifikacija)
                    }

                    inputLine = it.readLine()
                }
                it.close()
                println("RESPONSE > $response")
            }
        }
        return response
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView(viewPom)
        addDataSet()
    }

}

private fun addDataSet(){
    val data = notifikacije
    notificationAdapter.submitListNotification(data)
}

private fun initRecyclerView(view: View){

    view.fn_recycler_view.apply {
        layoutManager = LinearLayoutManager(view.context)
        notificationAdapter = NotificationsRecyclerAdapter()
        adapter = notificationAdapter
    }
}