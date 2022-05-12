package com.codingwithmitch.kotlinrecyclerviewexample


import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.skucise.R
import com.example.skucise.fragments.SveOceneF.Companion.ocene
import com.example.skucise.model.Ocena
import com.example.skucise.util.Constants
import kotlinx.android.synthetic.main.layout_rate_list_item.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.ArrayList


class RateRecyclerAdapter(korisnikPom: String?) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    var username = korisnikPom
    companion object
    {
        lateinit var idVlasnika: String //IZ SHARED PREFFS
        @SuppressLint("StaticFieldLeak")
        lateinit var viewPom : Context
    }

    private val TAG: String = "AppDebug"

    private var ocene: List<Ocena> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        viewPom = parent.context
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(viewPom)
        val usernameLogovanog = sharedPref.getString("Username", " ")

        var res = StringBuffer()
        res = sendGetRequestRate("oceneKorisnikaUsername/" + username)

        return RateViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_rate_list_item, parent, false)
        )
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder) {

            is RateViewHolder -> {
                holder.bind(ocene.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return ocene.size
    }

    fun submitListRate(rateList: List<Ocena>){
        ocene = rateList
    }

    class RateViewHolder
    constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){
        val ocena = itemView.rl_oglas_id
        val naslov_ocene = itemView.rl_naslov_oglasa
        val ocenio = itemView.rl_ocenu_dao
        val komentar = itemView.rl_comment
        val zvezdice = itemView.rl_rating


        @SuppressLint("SetTextI18n")
        fun bind(ratePost: Ocena){

            var book = JSONObject()
            book =  getOcenjivac("vlasnik/" + ratePost.idOcenjivaca)

            ocenio.setText("Ocena korisnika #" + book.get("username").toString())
            naslov_ocene.setText(ratePost.naslov)
            komentar.setText(ratePost.komentar)
            zvezdice.rating = ratePost.ocena.toFloat()
            zvezdice.isEnabled = false

        }
        //radi primer poziva je sendGetRequest("sviOglasi")
        @RequiresApi(Build.VERSION_CODES.O)
        //flag je broj u koju listu se puni liste su u zaglavlju (mutablelistof..)
        fun getOcenjivac(requestText: String): JSONObject {

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
    }

}


@RequiresApi(Build.VERSION_CODES.O)
fun  sendGetRequestRate(requestText :String) :StringBuffer{

    val mURL = URL(Constants.BASE_URL + "api/"+requestText)
    val response = StringBuffer()
    with(mURL.openConnection() as HttpURLConnection) {

        requestMethod = "GET"

        var obj = JSONArray()
        println("URL : $url")
        println("Response Code : $responseCode")
        BufferedReader(InputStreamReader(inputStream)).use {
            var inputLine = it.readLine()

            ocene.clear()
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
                }
                inputLine = it.readLine()
            }

            it.close()
            println("RESPONSE AdRecycle> $response")
        }
    }
    return response
}
