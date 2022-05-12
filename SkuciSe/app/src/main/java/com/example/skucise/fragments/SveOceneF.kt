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
import com.codingwithmitch.kotlinrecyclerviewexample.RateRecyclerAdapter
import com.example.skucise.R
import com.example.skucise.fragments.SveOceneF.Companion.ocene
import com.example.skucise.fragments.SveOceneF.Companion.rateAdapter
import com.example.skucise.model.Ocena
import com.example.skucise.util.Constants
import kotlinx.android.synthetic.main.fragment_ocene_korisnika.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SveOceneF(korisnik: String?, korisnikId: String?) : Fragment() {

    var korisnikPom = korisnik // TREBA IZLISTATI NJEGOVE OCENEEEE
    var korisnikIdPom = korisnikId

    lateinit var idVlasnika: String //IZ SHARED PREFFS
    companion object{
        lateinit var rateAdapter: RateRecyclerAdapter
        lateinit var viewPom : View
        var ocene = mutableListOf<Ocena>()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view : View = inflater!!.inflate(R.layout.fragment_ocene_korisnika, container, false)
        viewPom = view

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(view.context)
        //USERNAME LOGOVANE OSOBE KOJA KACI OGLAS
        val usernameLogovanog = sharedPref.getString("Username", " ")
        getKorisnik("vlasnikUser/" + korisnikPom)
        var res = StringBuffer()
        println("OceneKorisnika > "+ korisnikIdPom)
        res = sendGetRequestRate("oceneKorisnika/" + korisnikIdPom)

        if(usernameLogovanog == korisnikPom)
            view.fok_ocene_korisnika.setText("Moje ocene")
        else
            view.fok_ocene_korisnika.setText("Ocene korisnika " + korisnikPom)


        return view
    }
    private fun initRecyclerView(view: View){

        view.fok_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            rateAdapter = RateRecyclerAdapter(korisnikPom)
            adapter = rateAdapter
        }
    }
    /*TREBA ODRADITI NA BEKU POVLACENJE OCENA IZ BAZE*/

    @RequiresApi(Build.VERSION_CODES.O)
    private fun  sendGetRequestRate(requestText :String) :StringBuffer{

        val mURL = URL(Constants.BASE_URL + "api/"+requestText)
        val response = StringBuffer()
        with(mURL.openConnection() as HttpURLConnection) {

            requestMethod = "GET"

            var obj = JSONArray()
            println("URL : $url")
            println("Response Code : $responseCode")
            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()
//                ocene.clear()
                while (inputLine != null) {
                    response.append(inputLine)
                    obj = JSONArray(inputLine)


                    for (i in 0 until obj.length()) {
                        val book = obj.getJSONObject(i)

                        var ocena = Ocena(
                            book.get("idOcene").toString(),
                            book.get("idOcenjenog").toString(),
                            book.get("idOcenjivaca").toString(),
                            book.get("ocena").toString().toInt(),
                            book.get("komentar").toString(),
                            book.get("naslov").toString()
                        )
                        ocene.add(ocena)

                        println(ocene)
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
    val data = ocene
    rateAdapter.submitListRate(data)
}


