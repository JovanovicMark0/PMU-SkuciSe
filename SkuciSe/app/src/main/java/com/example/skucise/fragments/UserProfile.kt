package com.example.skucise.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.skucise.*
import com.example.skucise.activity.SveOcene
import com.example.skucise.model.Vlasnik
import com.example.skucise.util.Constants
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import okhttp3.internal.format
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.RoundingMode
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat
import kotlin.math.roundToLong

class UserProfile : Fragment() {

    companion object {
        lateinit var vlasnik: Vlasnik
        lateinit var slicica : Bitmap
        var responseFlag:Boolean = false
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_user_profile, container, false)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(view.context)
        val usernameLogovanog = sharedPref.getString("Username", " ")

        var obj = JSONObject()
        var book = JSONObject()


        book =  sendGetRequest("vlasnikUser/" + usernameLogovanog)

        vlasnik =  Vlasnik(
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


//ERDOGLIJA
/*        book = sendGetRequest("dajSlikuKorisnika/" + vlasnik.idVlasnika)
        if(book != null)
        {
            val decodedString: ByteArray = Base64.decode(book.get("slika").toString(),
                Base64.DEFAULT
            )
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            slicica = decodedByte
            view.fup_profile_picture_id.setImageBitmap(decodedByte)
        }
*/


        if (usernameLogovanog != null) {
            view.fup_usernameIdProfil.setText("DOBRODOŠAO " + usernameLogovanog.uppercase())
        }
        view.fup_profile_ime.setText("" + vlasnik.punoIme.toString())
        view.fup_profile_br_telefona.setText("" + vlasnik.brojTelefona)
        view.fup_profile_email.setText("" + vlasnik.email)
        if(vlasnik.brojOcena == 0)
            view.fup_ocena_avg.setText("Trenutno nemate ocena")
        else {
            val proba = ((vlasnik.zbirSvihOcena).toFloat()/ vlasnik.brojOcena)
            val decF = DecimalFormat("#.##")
            decF.roundingMode = RoundingMode.CEILING


            view.fup_ocena_avg.setText("" + decF.format(proba) + "/5")
            //view.profile_picture_id.setImageURI("https://images.unsplash.com/photo-1529665253569-6d01c0eaf7b6?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8cHJvZmlsZXxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=60")
        }

        view.fup_rating.rating = ((vlasnik.zbirSvihOcena).toFloat()/ vlasnik.brojOcena)
        view.fup_rating.isEnabled = false


        view.fup_sve_ocene.setText("Pogledaj sve ocene ("+vlasnik.brojOcena +")")


        view.fup_sve_ocene.setOnClickListener {
            if (vlasnik.brojOcena == 0)
            {
                Toast.makeText(context, "Trenutno nemate ni jednu ocenu.", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val intent = Intent(context, SveOcene::class.java)

                intent.putExtra("usernameVlasnika", usernameLogovanog)
                intent.putExtra("idVlasnika", vlasnik.idVlasnika)

                startActivity(intent)
            }
        }


        view.fup_svi_oglasi.setText("Svi oglasi ("+vlasnik.brojOglasa +")")

        view.fup_svi_oglasi.setOnClickListener {
            if (vlasnik.brojOglasa == 0)
            {
                Toast.makeText(context, "Trenutno nemate ni jedan postavljen oglas.", Toast.LENGTH_SHORT).show()
            }
            else
            {
                var fr = getFragmentManager()?.beginTransaction()?.addToBackStack(null)
                fr?.replace(R.id.fragment_container, MojiOglasi(vlasnik.idVlasnika))
                fr?.commit()
            }
        }
        view.fup_izmeni_podatke_btn.setOnClickListener {
            val intent = Intent(context, UserEditProfile::class.java)
//            intent.putExtra("slika" , slicica)
            startActivity(intent)
        }
        view.fup_logoutBTN.setOnClickListener{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()
            editor.clear()
            editor.commit()
            val i = Intent( context, MainActivity::class.java)
            startActivity(i)
        }

        var bookS = JSONObject()
        bookS = sendGetRequest("dajSlikuKorisnika/" + vlasnik.idVlasnika)
        if(responseFlag == true)
        {val decodedString: ByteArray = Base64.decode(bookS.get("slika").toString(), Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            view.fup_profile_picture_id.setImageBitmap(decodedByte)
            view.fup_profile_picture_id.setBackgroundResource(R.drawable.ic_baseline_check_box_outline_blank_24)
        }
        else
        {
            println("OK")
            view.fup_profile_picture_id.setBackgroundResource(R.drawable.ic_baseline_person_24)
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
            if(responseCode == 200)
            {
                responseFlag = true

                BufferedReader(InputStreamReader(inputStream)).use {

                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        obj = JSONObject(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                }}
                return obj

        }
    }

}
