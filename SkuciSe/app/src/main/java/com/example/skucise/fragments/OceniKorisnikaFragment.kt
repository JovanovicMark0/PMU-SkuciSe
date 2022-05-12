package com.example.skucise.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.skucise.*
import com.example.skucise.model.Ocena
import com.example.skucise.model.Vlasnik
import com.example.skucise.util.Constants
import com.example.skucise.util.MainViewModel
import com.example.skucise.util.MainViewModelFactory
import com.example.skucise.util.Repository
import kotlinx.android.synthetic.main.fragment_oceni_korisnika.view.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class OceniKorisnikaFragment(korisnik: String?, naslov: String?) : Fragment() {
    private lateinit var viewModel: MainViewModel

    var korisnikPom = korisnik
    var naslovPom = naslov
    companion object {
        lateinit var vlasnik: Vlasnik
    }


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_oceni_korisnika, container, false)




        val sharedPref = PreferenceManager.getDefaultSharedPreferences(view.context)
        val usernameLogovanog = sharedPref.getString("Username", " ")

        var obj = JSONObject()
        var book = JSONObject()


        book =  sendGetRequest("vlasnikUser/" + usernameLogovanog)
        obj  = sendGetRequest("vlasnikUser/" + korisnikPom)
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
        var ocenjenId = obj.get("idVlasnika").toString()

            view.fok_oceni_korisnika_tekst.setText("Oceni korisnika " + korisnikPom)
            view.fok_naslov_oglasa.setText(naslovPom.toString())
            view.fok_oceni_btn.setOnClickListener {

                val ocena = view.fok_rating.rating.toInt()

                if (ocena == 0)
                    Toast.makeText(
                        context,
                        "Niste uneli ocenu, pokušajte ponovo. ",
                        Toast.LENGTH_SHORT
                    ).show()
                else {
                    var komentaric = view.komentar.text.toString().trim()
                    var ocenica = Ocena("522296bd-a404-4c72-8100-76ad1e5ceee5" ,ocenjenId,vlasnik.idVlasnika,ocena,komentaric, naslovPom.toString())
                    //RETROFIT S
                    val repository = Repository()

                    val viewModelFactory = MainViewModelFactory(repository)

                    viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

                    //dodavanje Stana
                    viewModel.dodajOcenu(ocenica)

                    viewModel.myResponse.observe(
                        viewLifecycleOwner,
                        Observer { response ->
                            if (response.isSuccessful) {
                                Log.d("Main", response.body().toString())
                                Log.d("Main", response.code().toString())
                                Log.d("Main", response.message().toString())
                            } else {
                                //Toast.makeText(view.context, response.code(), Toast.LENGTH_SHORT).show()
                                println("ERROR 592$1001 CTRL IVAN")
                            }
                        })


                    //RETROFIT E


                    Toast.makeText(
                        context,
                        "Uspešno ste ocenili korisnika " + korisnikPom,
                        Toast.LENGTH_SHORT
                    ).show()



                    val intent = Intent(context, HomePage::class.java)
                    startActivity(intent)
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
