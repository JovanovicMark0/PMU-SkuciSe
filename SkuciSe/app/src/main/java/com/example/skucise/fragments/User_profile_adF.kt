package com.example.skucise.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.skucise.R
import com.example.skucise.activity.OglasPage
import com.example.skucise.activity.SveOcene
import com.example.skucise.model.Vlasnik
import com.example.skucise.util.Constants
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_edit_profile.view.*
import kotlinx.android.synthetic.main.activity_oglas_page.*
import kotlinx.android.synthetic.main.fragment_user_display.view.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.RoundingMode
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat

class User_profile_adF(usernameVlasnika: String?) : Fragment() {

    companion object {
        lateinit var korisnik: Vlasnik
        var responseFlag : Boolean = false
    }
    val unVlasnika = usernameVlasnika
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_user_display, container, false)


        var obj = JSONObject()
        var book = JSONObject()


        book =  sendGetRequest("vlasnikUser/" + unVlasnika)

        korisnik =  Vlasnik(
            book.get("idVlasnika").toString(),
            book.get("username").toString(),
            book.get("punoIme").toString(),
            book.get("brojTelefona").toString(),
            (book.get("brojOglasa").toString()).toInt(),
            (book.get("zbirSvihOcena").toString()).toInt(),
            (book.get("brojOcena").toString()).toInt(),
            book.get("email").toString(),
            book.get("password").toString()
        )


        var bookS = JSONObject()
        bookS = sendGetRequest("dajSlikuKorisnika/" + korisnik.idVlasnika)
        if(responseFlag == true)
        {val decodedString: ByteArray = Base64.decode(bookS.get("slika").toString(), Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            view.fud_profile_picture.setImageBitmap(decodedByte)
            view.fud_profile_picture.setBackgroundResource(R.drawable.ic_baseline_check_box_outline_blank_24)
        }
        else
        {
            println("OK")
            view.fud_profile_picture.setBackgroundResource(R.drawable.ic_baseline_person_24)
        }

        view.fud_usernameIdProfil.setText("Podaci korisnika " + korisnik.username)
        view.fud_profile_ime.setText("" + korisnik.punoIme.toString())
        view.fud_profile_br_telefona.setText("" + korisnik.brojTelefona)
        view.fud_profile_email.setText("" + korisnik.email)

        if(korisnik.brojOcena == 0)
            view.fud_ocena_avg.setText("Korisnik nema ocene.")
        else {
            val proba = ((korisnik.zbirSvihOcena).toFloat()/korisnik.brojOcena)
            val decF = DecimalFormat("#.##")
            decF.roundingMode = RoundingMode.CEILING
            view.fud_ocena_avg.setText("" + decF.format(proba) + "/5")
        }

        view.fud_rating.rating = ((korisnik.zbirSvihOcena).toFloat()/korisnik.brojOcena)
        view.fud_rating.isEnabled = false

        view.fud_ocene_korisnika.setText("Pogledaj ocene korisnika (" + korisnik.brojOcena +")")
        view.fud_ocene_korisnika.setOnClickListener{
            if(korisnik.brojOcena == 0)
                Toast.makeText(context, "Korisnik nije do sada ocenjivan", Toast.LENGTH_SHORT).show()
            else
            {
                val intent = Intent(context, SveOcene::class.java)
                intent.putExtra("usernameVlasnika", unVlasnika)
                intent.putExtra("idVlasnika", korisnik.idVlasnika)
                startActivity(intent)
            }


        }

        view.fud_oglasi_korisnika.setText("Pogledaj oglase korisnika ("+korisnik.brojOglasa +")")

        view.fud_oglasi_korisnika.setOnClickListener {
            if (korisnik.brojOglasa == 0)
            {
                Toast.makeText(context, "Korisnik nema ni jedan postavljen oglas.", Toast.LENGTH_SHORT).show()
            }
            else
            {
                var fr = getFragmentManager()?.beginTransaction()?.addToBackStack(null)
                fr?.replace(R.id.fragment_container, MojiOglasi(korisnik.idVlasnika))
                fr?.commit()
                /*
                val intent = Intent(context, SviOglasi::class.java)

                intent.putExtra("usernameVlasnika", usernameLogovanog)
                intent.putExtra("idVlasnika", vlasnik.idVlasnika)

                startActivity(intent)*/
            }
        }



        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendGetRequest(requestText: String): JSONObject {

        val mURL = URL(Constants.BASE_URL + "api/" + requestText)
        var obj = JSONObject()
        with(mURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "GET"

            println("URL : $url")
            println("Response Code : $responseCode")
            responseFlag = false
            if(responseCode == 200){
                responseFlag = true
                BufferedReader(InputStreamReader(inputStream)).use {
                    var inputLine = it.readLine()
                    while(inputLine != null)
                    {
                        obj = JSONObject(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()}
            }
                return obj

        }
    }
}
