package com.codingwithmitch.kotlinrecyclerviewexample


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.skucise.R
import com.example.skucise.User_profile_ad
import com.example.skucise.activity.OglasPage
import com.example.skucise.fragments.Notifications.Companion.notifikacije
import com.example.skucise.model.Obavestenje
import com.example.skucise.util.Constants
import kotlinx.android.synthetic.main.layout_notifications_list_item.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import kotlin.collections.ArrayList


class NotificationsRecyclerAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    companion object
    {
        lateinit var idVlasnika: String //IZ SHARED PREFFS
        @SuppressLint("StaticFieldLeak")
        lateinit var viewPom : Context
        lateinit var usernameLogovanog : String
    }

    private val TAG: String = "AppDebug"

    private var notifikacije: List<Obavestenje> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        viewPom = parent.context
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(viewPom)
        usernameLogovanog = sharedPref.getString("Username", " ").toString()
        var res = StringBuffer()
        res = sendGetRequestNotifications("svaObavestenjaKorisnika/" + usernameLogovanog)


        return NotificationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_notifications_list_item, parent, false)
        )
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder) {

            is NotificationViewHolder -> {
                holder.bind(notifikacije.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return notifikacije.size
    }

    fun submitListNotification(notificationList: List<Obavestenje>){
        notifikacije = notificationList
    }

    class NotificationViewHolder
    constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){

        val oglas = itemView.nl_oglas
        val ponudjacZaKlik = itemView.nl_ponudjac

        val ponudjac = itemView.nl_ponudjac_text



        val naslov_oglasa = itemView.nl_naslov_oglasa
        val opis = itemView.nl_opis_oglasa

        val accept = itemView.nl_accept
        val decline = itemView.nl_decline
        val ceoDeo = itemView.notifikacija_id



        @SuppressLint("SetTextI18n")
        fun bind(notificationPost: Obavestenje) {
            ponudjac.text =
                "Zahtev za pregled objekta od korisnika #" + notificationPost.usernamePosiljaoca
            naslov_oglasa.text = notificationPost.naslovOglasa
            opis.text = notificationPost.datum

            oglas.setOnClickListener {
                val intent = Intent(it.getContext(), OglasPage::class.java)
                intent.putExtra("id", notificationPost.idOglasa.toString().trim())
                it.getContext().startActivity(intent)

            }

            var ponudjac = JSONObject()
            ponudjac = getOcenjivac("vlasnik/" + notificationPost.idPosiljaoca)


            ponudjacZaKlik.setOnClickListener {
                val intent = Intent(it.context, User_profile_ad::class.java)
                intent.putExtra("usernameVlasnika", ponudjac.get("username").toString())
                it.context.startActivity(intent)
            }

            //KOD POSILJAOCA
            if (notificationPost.usernamePosiljaoca.equals(usernameLogovanog))
            {
                if(notificationPost.prihvacen == 1)
                {
                    accept.isVisible = false
                    accept.isEnabled= false
                    decline.isVisible = false
                    decline.isEnabled= false

                    ceoDeo.setBackgroundColor(Color.parseColor("#beff8f"))
                }
                else
                {
                    accept.isVisible = false
                    accept.isEnabled= false
                    decline.isVisible = false
                    decline.isEnabled= false
                }
            }//KOD PRIMAOCA
            else {
                if(notificationPost.prihvacen == 1) {
                    accept.isVisible = false
                    accept.isEnabled = false
                    decline.isVisible = false
                    decline.isEnabled = false

                    ceoDeo.setBackgroundColor(Color.parseColor("#beff8f"))
                }
            }

                //PRIHVACEN
                accept.setOnClickListener {
                    var book = JSONObject()
                    var bul: Boolean = true
                    getOcenjivac("izmeniObavestenje/" + notificationPost.idObavestenja + "/" + bul)
                    accept.isEnabled = false
                    decline.isVisible = false
                    decline.isEnabled = false
                    ceoDeo.setBackgroundColor(Color.parseColor("#beff8f"))
                }

                //ODBIJEN
                decline.setOnClickListener {
                    var book = JSONObject()
                    var bul: Boolean = false
                    getOcenjivac("izmeniObavestenje/" + notificationPost.idObavestenja + "/" + bul)
                    accept.isEnabled = false
                    accept.isVisible = false
                    ceoDeo.setBackgroundColor(Color.parseColor("#ff8a8a"))
                    decline.isEnabled = false
                    //notifikacije.remove(notificationPost)
                }


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
fun  sendGetRequestNotifications(requestText :String) :StringBuffer{

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


                    var notifikacija = Obavestenje(
                        book.get("idObavestenja").toString(),
                        book.get("idOglasa").toString(),
                        book.get("idPosiljaoca").toString(),
                        book.get("idPrimaoca").toString(),
                        book.get("datum").toString(),
                        book.get("prihvacen").toString().toInt(),
                        book.get("usernamePosiljaoca").toString(),
                        book.get("naslovOglasa").toString()
                    )
                    notifikacije.add(notifikacija)
                    println(notifikacija)
                }
                inputLine = it.readLine()
            }

            it.close()
            println("RESPONSE AdRecycle> $response")
        }
    }
    return response
}