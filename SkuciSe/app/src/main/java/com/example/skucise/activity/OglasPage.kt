package com.example.skucise.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.preference.PreferenceManager
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.kotlinrecyclerviewexample.AdRecyclerAdapter
import com.example.skucise.HomePage
import com.example.skucise.OceniKorisnika
import com.example.skucise.R
import com.example.skucise.User_profile_ad
import com.example.skucise.fragments.Filteri
import com.example.skucise.fragments.Home
import com.example.skucise.fragments.UserProfile
import com.example.skucise.fragments.User_profile_adF
import com.example.skucise.model.*
import com.example.skucise.util.Constants
import com.example.skucise.util.MainViewModel
import com.example.skucise.util.MainViewModelFactory
import com.example.skucise.util.Repository
import kotlinx.android.synthetic.main.activity_edit_profile.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_oglas_page.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import kotlinx.android.synthetic.main.activity_oglas_page.brojSoba
import kotlinx.android.synthetic.main.activity_oglas_page.dugmePrethodnaSlika
import kotlinx.android.synthetic.main.activity_oglas_page.dugmeSledecaSlika
import kotlinx.android.synthetic.main.activity_oglas_page.gradnja
import kotlinx.android.synthetic.main.activity_oglas_page.kategorija
import kotlinx.android.synthetic.main.activity_oglas_page.slike
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import kotlinx.android.synthetic.main.fragment_filteri.*
import kotlinx.android.synthetic.main.fragment_filteri.view.*
import kotlinx.android.synthetic.main.fragment_user_display.view.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import org.json.JSONArray
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class OglasPage : AppCompatActivity() {

    companion object{
        lateinit var oglas : Oglas
        lateinit var stan : Stan
        lateinit var vlasnik : Vlasnik
        lateinit var ostalo : Ostalo
        lateinit var date : LocalDateTime

    }
    private var images: ArrayList<Bitmap?>? = null
    private var position = 0
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oglas_page)
        var idOglasa: String? = intent.getStringExtra("id")


        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val usernameLogovanog = sharedPref.getString("Username", " ")

        //OBAVEZNO ZA SVE STO POZIVA API (u onCreate)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        //----------------------------

        //Ucitavanje oglasa iz baze

        var obj = JSONObject()
        var book = JSONObject()
        book = sendGetRequest("vlasnikUser/"+usernameLogovanog) //OVDEEEEEE
        var ulogovaniID = book.get("idVlasnika").toString()
        //OGLAS // TREBA ZAMENITI SVI OGLASI ZA POSEBAN OGLAS :) KADA SE KLIKNE NA OGLAS OTVARA OVO OVDE
        book = sendGetRequest("oglas/"+idOglasa.toString().trim()) //OVDEEEEEE

        date = LocalDateTime.parse(book.get("datumIsteka").toString())

        oglas = Oglas(
            book.get("idOglasa").toString(),
            book.get("idVlasnika").toString(),
            book.get("idStana").toString(),
            book.get("naslovOglasa").toString(),
            book.get("opisOglasa").toString(),
            (book.get("cena").toString()).toInt(),
            date,
            (book.get("prodajaDaNe").toString()).toInt(),
            (book.get("brojLajkova").toString()).toInt(),
            (book.get("brojPregleda").toString()).toInt()
        )
        ostalo =  Ostalo(
            book.get("nazivGrada").toString(),
            book.get("tipGradnje").toString(),
            book.get("namestenost").toString(),
            book.get("stanje").toString(),
            book.get("tipStana").toString(),
        )
        println(ostalo)
        println(oglas)


        //STAN
        book =  sendGetRequest("stan/${oglas.idStana}")

        println(book)
        stan =  Stan(
            book.get("idStana").toString(),
            book.get("idVlasnika").toString(),
            (book.get("povrsina").toString()).toInt(),
            book.get("grad").toString(),
            book.get("adresa").toString(),
            (book.get("brojSoba").toString()).toInt(),
            (book.get("idTipaStana").toString()).toInt(),
            (book.get("idStanjaObjekta").toString()).toInt(),
            (book.get("idNamestenosti").toString()).toInt(),
            (book.get("idGradnje").toString()).toInt(),
            (book.get("sprat").toString()).toInt(),
            (book.get("terasa").toString()).toInt(),
            (book.get("telefon").toString()).toInt(),
            (book.get("internet").toString()).toInt(),
            (book.get("katv").toString()).toInt(),
            (book.get("interfon").toString()).toInt(),
            (book.get("lift").toString()).toInt(),
            (book.get("klima").toString()).toInt(),
            (book.get("grejanje").toString()).toInt(),
            (book.get("parking").toString()).toInt(),
            (book.get("videoNadzor").toString()).toInt()
        )
        println(stan)


        //VLASNIK
        book =  sendGetRequest("vlasnik/${oglas.idVlasnika}")

        vlasnik =  Vlasnik(
            book.get("idVlasnika").toString(),
            book.get("username").toString(),
            book.get("punoIme").toString(),
            book.get("brojTelefona").toString(),
            (book.get("brojOglasa").toString()).toInt(),
            (book.get("zbirSvihOcena").toString()).toInt(),
            (book.get("brojOcena").toString()).toInt(),
            book.get("email").toString(),
            book.get("password").toString())
        println(vlasnik)


        var fullOglas = FullOglas(
            oglas,
            stan,
            vlasnik,
            ostalo
        )
        println(fullOglas)

        //odavde stavljati na front
        if(oglas.prodajaDaNe==1)
        {
            op_sale_yesNo.setText("Na prodaju")
        }
        op_broj_pregleda.text = " " + oglas.brojPregleda.toString()
        op_lajkova.text = " " + oglas.brojLajkova.toString()
        op_naziv_oglasa.setText(oglas.naslovOglasa)
        println(oglas.naslovOglasa)
        op_opis.setText(oglas.opisOglasa)
        op_cena.setText(oglas.cena.toString())
        op_povrsina.setText(stan.povrsina.toString())
        brojSoba.setText(stan.brojSoba.toString())
        op_brojSpratova.setText(stan.sprat.toString())
        op_location.setText(ostalo.nazivGrada)

        op_username_korisnika.setText(vlasnik.username)
        op_grad_korisnika.setText(stan.grad)
        if(vlasnik.brojOcena == 0)
            op_prosek_ocena.setText("Korisnik nema ocene.")
        else {
            val proba = ((vlasnik.zbirSvihOcena).toFloat()/ vlasnik.brojOcena)
            val decF = DecimalFormat("#.##")
            decF.roundingMode = RoundingMode.CEILING

            op_prosek_ocena.setText("" + decF.format(proba) + "/5")
        }


        if(stan.idNamestenosti==1)
        {
            op_namestenost.setText("Nenamešteno")

        }
        else if (stan.idNamestenosti==2)
        {
            op_namestenost.setText("Polunamešteno")
        }
        else if(stan.idNamestenosti==3)
        {
            op_namestenost.setText("Namešteno")
        }

        if(stan.idStanjaObjekta==1)
        {
            op_stanje.setText("Izvorno")
        }
        else if(stan.idStanjaObjekta==2)
        {
            op_stanje.setText("Renovirano")
        }
        else if(stan.idStanjaObjekta==3)
        {
            op_stanje.setText("Potrebno renoviranje")
        }
        if (stan.idGradnje==1)
        {
            gradnja.setText("Stara gradnja")
        }
        else if(stan.idGradnje==2)
        {
            gradnja.setText("Nova gradnja")
        }

        if(stan.idTipaStana == 1)
            kategorija.setText("Garsonjera")
        else if(stan.idTipaStana == 2)
            kategorija.setText("Stan")
        else if(stan.idTipaStana == 3)
            kategorija.setText("Kuća")
        else if(stan.idTipaStana == 4)
            kategorija.setText("Poslovni prostor")
        else if(stan.idTipaStana == 5)
            kategorija.setText("Vikendica")


        if(stan.terasa ==1)
        {

            terasa_check.setImageResource(R.drawable.check)
        }
        if(stan.grejanje==1)
        {
            grejanje_check.setImageResource(R.drawable.check)
        }
        if(stan.internet==1)
        {
            internet_check.setImageResource(R.drawable.check)
        }
        if(stan.interfon==1)
        {
            interfon_check.setImageResource(R.drawable.check)
        }
        if(stan.katv==1)
        {
            KATV_check.setImageResource(R.drawable.check)
        }
        if(stan.klima==1)
        {
            klima_check.setImageResource(R.drawable.check)
        }
        if(stan.lift==1)
        {
            lift_check.setImageResource(R.drawable.check)
        }
        if (stan.parking==1)
        {
            parking_check.setImageResource(R.drawable.check)
        }
        if(stan.telefon==1)
        {
            telefon_check.setImageResource(R.drawable.check)
        }
        if (stan.videoNadzor==1)
        {
            video_nadzor_check.setImageResource(R.drawable.check)
        }




        val profilOglasivaca = findViewById<TextView>(R.id.op_profil_oglasivaca)
        profilOglasivaca.setOnClickListener {

            val intent = Intent(this, User_profile_ad::class.java)

            intent.putExtra("usernameVlasnika", vlasnik.username)
            startActivity(intent)
        }

        images = ArrayList()
        getImages("dajSveSlikeOglasa/"+oglas.idOglasa)
        if(images!!.size > 0)
        {
            slike.setImageBitmap(images!![0])
            //slike.setImageURI(images!![0])
            slike.setBackgroundResource(R.drawable.ic_baseline_check_box_outline_blank_24)

        }
        position = 0
        //ImageSwitcher

        dugmeSledecaSlika.setOnClickListener {
            if (position < images!!.size - 1) {
                position++
                slike.setImageBitmap(images!![position])
            } else {
                Toast.makeText(this, "Nema više slika...", Toast.LENGTH_SHORT).show()
            }
        }

        dugmePrethodnaSlika.setOnClickListener {
            if (position > 0) {
                position--
                slike.setImageBitmap(images!![position])
            } else {
                Toast.makeText(this, "Nema više slika...", Toast.LENGTH_SHORT).show()
            }
        }


        val pozoviOglasivaca = findViewById<Button>(R.id.op_pozovi)
        val zatraziPosetu = findViewById<Button>(R.id.op_zatrazi_posetu)
        val izmeniOglas = findViewById<Button>(R.id.op_izmeni_oglas)
        val obrisiOglas = findViewById<ImageView>(R.id.op_remove)
        val oceniOglasivaca = findViewById<TextView>(R.id.op_oceni_oglasivaca)

        if(usernameLogovanog == vlasnik.username)// ne moze korisnik sam sebe da oceni
        {
            pozoviOglasivaca.isVisible = false
            zatraziPosetu.isVisible = false
            op_oceni_oglasivaca.isVisible = false

            izmeniOglas.setOnClickListener {
                val intent = Intent(this, EditOglasa::class.java)
                intent.putExtra("idOglasaa", idOglasa)
                startActivity(intent)
            }

            obrisiOglas.setOnClickListener{
                sendGetRequest("brisiOglas/" + idOglasa)
                val intent = Intent(this, HomePage::class.java)
                startActivity(intent)
            }
        }
        else {


            op_remove.isVisible = false
            izmeniOglas.isVisible = false

            pozoviOglasivaca.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.setData(Uri.parse("tel:" + vlasnik.brojTelefona))
                startActivity(intent)
            }

            val c = Calendar.getInstance()
            val godina = c.get(Calendar.YEAR)
            val mesec = c.get(Calendar.MONTH)
            val dan = c.get(Calendar.DAY_OF_MONTH)


            op_zatrazi_posetu.setOnClickListener {

                val dpd = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                        var notifikacijaZaUpis = usernameLogovanog?.let { it1 ->
                            Messages(
                                "neki string za id",
                                oglas.idOglasa,
                                "" + mDay + "/" + (mMonth+1) + "/" + mYear,
                                it1,
                                oglas.idVlasnika
                            )


                            var obavestenje : Obavestenje = Obavestenje(
                                "17aecccb-7a58-4ad7-85e7-8fb5d5da84fa",oglas.idOglasa,ulogovaniID,oglas.idVlasnika,"" + mDay + "/" + (mMonth+1) + "/" + mYear,0,usernameLogovanog, oglas.naslovOglasa)
//RETROFIT S
                            val repository = Repository()

                            val viewModelFactory = MainViewModelFactory(repository)
                            var viewModel: MainViewModel = ViewModelProvider(this, viewModelFactory).get(
                                MainViewModel::class.java)

                            //dodavanje obavestenja
                            viewModel.dodajObavestenje(obavestenje)

                            viewModel.myResponse1.observe(
                                this,
                                androidx.lifecycle.Observer { response ->
                                    if (response.isSuccessful) {
                                        Log.d("Main", response.body().toString())
                                        Log.d("Main", response.code().toString())
                                        Log.d("Main", response.message().toString())

                                        if(response.code() == 200)
                                        {
                                            Toast.makeText(this, "Uspešno ste zatražili posetu za datum " + mDay + "/" + (mMonth+1) + "/" + mYear, Toast.LENGTH_SHORT).show()
                                        }
                                        else{
                                            Toast.makeText(view.context, "Greška", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        //Toast.makeText(view.context, response.code(), Toast.LENGTH_SHORT).show()
                                        println("ERROR 600$5")
                                    }
                                })


                            //RETROFIT E


                        }
                    }
                    , godina,mesec,dan)

                dpd.show()
            }


            oceniOglasivaca.setOnClickListener {

                val intent = Intent(this, OceniKorisnika::class.java)

                intent.putExtra("usernameVlasnika", vlasnik.username)
                intent.putExtra("naslovOglasa", oglas.naslovOglasa)
                // ovo odraditi tako sto se salje id oglasa
                startActivity(intent)
            }

        }



    }

    override fun onBackPressed() {
        val intent = Intent(this, HomePage::class.java)
        this.startActivity(intent)
    }
/*
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
*/

    //radi primer poziva je sendGetRequest("sviOglasi")
    @RequiresApi(Build.VERSION_CODES.O)
    //flag je broj u koju listu se puni liste su u zaglavlju (mutablelistof..)
    fun sendGetRequest(requestText: String): JSONObject {

        val mURL = URL(Constants.BASE_URL + "api/" + requestText)
        var obj = JSONObject()
        with(mURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "GET"

            println("URL : $url")
            println("Response Code : $responseCode")
            if(responseCode == 200)
            {
            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()
                while(inputLine != null)
                {
                    obj = JSONObject(inputLine)
                    inputLine = it.readLine()
                }
                it.close()}}
                return obj

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun  getImages(requestText :String){

        val mURL = URL(Constants.BASE_URL + "api/"+requestText)
        val response = StringBuffer()
        with(mURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "GET"

            var obj = JSONArray()
            println("URL : $url")
            println("Response Code : $responseCode")
            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()

                Home.oglasi.clear()
                while (inputLine != null) {
                    response.append(inputLine)
                    obj = JSONArray(inputLine)



                    for (i in 0 until obj.length()) {
                        val book = obj.getJSONObject(i)

                        //var slika = SlikaOglasa(book.get("idSlike").toString(),book.get("idSlike").toString(), book.get("slika").toString())
                        //images?.add(slika.slika.toUri())
                        val decodedString: ByteArray = Base64.decode(book.get("slika").toString(),
                            Base64.DEFAULT
                        )
                        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                        images?.add(decodedByte)
                        println(oglas)
                    }
                    //var oglas =  Oglas(obj[1][0])


                    inputLine = it.readLine()
                }
                it.close()
                println("RESPONSE > $response")
            }
        }
    }

}

