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
import com.example.skucise.activity.OglasPage
import com.example.skucise.fragments.Like.Companion.adAdapter
import com.example.skucise.fragments.MojiOglasi.Companion.mojiOglasi
import com.example.skucise.model.Oglas
import com.example.skucise.util.Constants
import kotlinx.android.synthetic.main.aactivity_main.view.*
import kotlinx.android.synthetic.main.aactivity_main.view.recycler_view
import kotlinx.android.synthetic.main.fragment_moji_oglasi.view.*
import kotlinx.android.synthetic.main.fragment_ocene_korisnika.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime

class MojiOglasi(idVlasnikaa: String) : Fragment() {
    var idVlasnikaPom = idVlasnikaa
    lateinit var idVlasnika: String //IZ SHARED PREFFS
    companion object{
        lateinit var adAdapter: AdRecyclerAdapter
        lateinit var viewPom : View
        var mojiOglasi = mutableListOf<Oglas>()
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view : View = inflater!!.inflate(R.layout.fragment_moji_oglasi, container, false)
        viewPom = view

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(view.context)
        //USERNAME LOGOVANE OSOBE KOJA KACI OGLAS
        val usernameLogovanog = sharedPref.getString("Username", " ")
        getKorisnik("vlasnikUser/" + usernameLogovanog)
        var res = StringBuffer()
        res = sendGetRequest("sviOglasiKorisnika/" + idVlasnikaPom)


        var book = JSONObject()
        book =  sendGetRequestVlasnik("vlasnik/" + idVlasnikaPom)

        if(idVlasnikaPom == idVlasnika)
            view.mo_naslov.setText("Moji oglasi")
        else
            view.mo_naslov.setText("Oglasi korisnika " + book.get("username").toString().uppercase())


        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    //flag je broj u koju listu se puni liste su u zaglavlju (mutablelistof..)
    fun sendGetRequestVlasnik(requestText: String): JSONObject {

        val mURL = URL(Constants.BASE_URL + "api/" + requestText)
        var obj = JSONObject()
        with(mURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "GET"

            println("URL : $url")
            println("Response Code : $responseCode")
            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()
                while(inputLine != null)
                {
                    obj = JSONObject(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                return obj
            }
        }
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
                mojiOglasi.clear()
                while (inputLine != null) {
                    response.append(inputLine)
                    obj = JSONArray(inputLine)


                    for (i in 0 until obj.length()) {
                        val book = obj.getJSONObject(i)

                        var date = LocalDateTime.parse(book.get("datumIsteka").toString())

                        var oglas = Oglas(book.get("idOglasa").toString(), book.get("idVlasnika").toString(), book.get("idStana").toString() ,book.get("naslovOglasa").toString(), book.get("opisOglasa").toString() , (book.get("cena").toString()).toInt() , date ,(book.get("prodajaDaNe").toString()).toInt() ,(book.get("brojLajkova").toString()).toInt() , (book.get("brojPregleda").toString()).toInt())
                        mojiOglasi.add(oglas)
                        println(oglas)
                    }

                    inputLine = it.readLine()
                }
                it.close()
            }
        }
        return response
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView(viewPom)
        addDataSet()
    }


    //GET REQ
    @RequiresApi(Build.VERSION_CODES.O)
    fun getKorisnik(requestText: String): StringBuffer {

        val mURL = URL(Constants.BASE_URL + "api/" + requestText)
        val response = StringBuffer()
        with(mURL.openConnection() as HttpURLConnection) {

            requestMethod = "GET"

            var obj = JSONObject()
            println("URL : $url")
            println("Response Code : $responseCode")
            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()
                while (inputLine != null) {
                    obj = JSONObject(inputLine)
                    inputLine = it.readLine()
                    idVlasnika = obj.get("idVlasnika").toString()
                }
                it.close()

                println("RESPONSE > $response")
            }
        }
        return response
    }
}

private fun addDataSet(){
    val data = mojiOglasi
    adAdapter.submitList(data)
}

private fun initRecyclerView(view: View){

    view.recycler_view.apply {
        layoutManager = LinearLayoutManager(context)
        adAdapter = AdRecyclerAdapter()
        adapter = adAdapter
    }
}
