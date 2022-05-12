package com.example.skucise.fragments

import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingwithmitch.kotlinrecyclerviewexample.AdRecyclerAdapter
import com.example.skucise.R
import com.example.skucise.fragments.Like.Companion.adAdapter
import com.example.skucise.fragments.Like.Companion.oglasi1
import com.example.skucise.model.Oglas
import com.example.skucise.util.Constants
import kotlinx.android.synthetic.main.aactivity_main.view.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime

class Like : Fragment() {

    companion object{
        lateinit var adAdapter: AdRecyclerAdapter
        lateinit var viewPom : View
        var oglasi1 = mutableListOf<Oglas>()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view : View = inflater!!.inflate(R.layout.fragment_like, container, false)
        viewPom = view

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(view.context)
        val usernameLogovanog = sharedPref.getString("Username", " ")
        var res = StringBuffer()
        res = sendGetRequest("omiljeniOglasi/" + usernameLogovanog)


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
                oglasi1.clear()
                while (inputLine != null) {
                    response.append(inputLine)
                    obj = JSONArray(inputLine)


                    for (i in 0 until obj.length()) {
                        val book = obj.getJSONObject(i)

                        var date = LocalDateTime.parse((book.get("datumIsteka").toString()).trim())

                        var oglas = Oglas(book.get("idOglasa").toString(), book.get("idVlasnika").toString(), book.get("idStana").toString() ,book.get("naslovOglasa").toString(), book.get("opisOglasa").toString() , (book.get("cena").toString()).toInt() , date ,(book.get("prodajaDaNe").toString()).toInt() ,(book.get("brojLajkova").toString()).toInt(),(book.get("brojPregleda").toString()).toInt() )
                        oglasi1.add(oglas)
                        println(oglas)
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
    val data = oglasi1
    adAdapter.submitList(data)
}

private fun initRecyclerView(view: View){

    view.recycler_view.apply {
        layoutManager = LinearLayoutManager(context)
        adAdapter = AdRecyclerAdapter()
        adapter = adAdapter
    }
}