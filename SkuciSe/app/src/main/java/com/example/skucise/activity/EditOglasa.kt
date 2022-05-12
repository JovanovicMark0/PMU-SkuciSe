package com.example.skucise.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.preference.PreferenceManager
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.skucise.HomePage
import com.example.skucise.R
import com.example.skucise.model.*
import com.example.skucise.util.Constants
import com.example.skucise.util.MainViewModel
import com.example.skucise.util.MainViewModelFactory
import com.example.skucise.util.Repository
import kotlinx.android.synthetic.main.edit_oglas_page.*
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import kotlin.properties.Delegates

class EditOglasa  : AppCompatActivity() {


    companion object{
        private lateinit var viewModel: MainViewModel
        lateinit var oglas : Oglas
        lateinit var izmenjeniOglas : Oglas
        lateinit var izmenjeniStan : Stan

        lateinit var stan : Stan
        lateinit var vlasnik : Vlasnik
        lateinit var ostalo : Ostalo
        lateinit var date : LocalDateTime

    }
    private var images: ArrayList<Uri?>? = null
    private var position = 0
    var slikeOglasa = mutableListOf<SlikaOglasa>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_oglas_page)
        var idOglasa: String? = intent.getStringExtra("idOglasaa")


        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val usernameLogovanog = sharedPref.getString("Username", " ")

        //OBAVEZNO ZA SVE STO POZIVA API (u onCreate)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        //----------------------------
        //ucitavanje slika
        images = ArrayList()

        adedit_slike.setFactory { ImageView(this) }

        adedit_slike.setOnClickListener {
            pickImagesIntent()
            adedit_slike.setBackgroundResource(R.drawable.ic_baseline_check_box_outline_blank_24)
        }

        adedit_sledecaSlika.setOnClickListener {
            if (position < images!!.size - 1) {
                position++
                adedit_slike.setImageURI(images!![position])
            } else {
                Toast.makeText(this, "Nema više slika...", Toast.LENGTH_SHORT).show()
            }
        }

        adedit_prethodnaSlika.setOnClickListener {
            if (position > 0) {
                position--
                adedit_slike.setImageURI(images!![position])
            } else {
                Toast.makeText(this, "Nema više slika...", Toast.LENGTH_SHORT).show()
            }
        }

        //Ucitavanje oglasa iz baze

        var obj = JSONObject()
        var book = JSONObject()

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
        if(oglas.prodajaDaNe == 1)
        {
            adedit_prodaja.isChecked = true
        }
        else
        {
            adedit_izdavanje.isChecked = true
        }


        //fokus na spiner GARSONJERA
        adedit_kategorija.setSelection(stan.idTipaStana-1)


        adedit_naslov_naslov.setText(oglas.naslovOglasa)

        adedit_opis.setText(oglas.opisOglasa)

        //spinner za grad NE RADII


        if(stan.adresa.isEmpty())
        {
            adedit_adresa.setText(stan.adresa)
        }
        else
        {
            adedit_adresa.setText("Adresa")
        }
        adedit_cena.setText(oglas.cena.toString())

        // stanje namesten
        adedit_namestenost.setSelection(stan.idNamestenosti-1)


        // izvorno stanje
        adedit_stanjeObjekta.setSelection(stan.idStanjaObjekta-1)


        //nova gradnja
        adedit_gradnja.setSelection(stan.idGradnje-1)

        adedit_povrsina.setText(stan.povrsina.toString())

        adedit_brojSpratova.setText(stan.sprat.toString())

        adedit_brojSoba.setText(stan.brojSoba.toString())


        if(stan.terasa ==1)
        {
            adedit_terasa.isChecked = true
        }
        if(stan.grejanje==1)
        {
            adedit_grejanje.isChecked = true
        }
        if(stan.internet==1)
        {
            adedit_internet.isChecked = true
        }
        if(stan.interfon==1)
        {
            adedit_interfon.isChecked = true
        }
        if(stan.katv==1)
        {
            adedit_katv.isChecked = true
        }
        if(stan.klima==1)
        {
            adedit_klima.isChecked = true
        }
        if(stan.lift==1)
        {
            adedit_lift.isChecked = true
        }
        if (stan.parking==1)
        {
            adedit_parking.isChecked = true
        }
        if(stan.telefon==1)
        {
            adedit_telefon.isChecked = true
        }
        if (stan.videoNadzor==1)
        {
            adedit_video_nadzor.isChecked = true
        }

        val sacuvajOglas = findViewById<Button>(R.id.adedit_izmeni_btn)




        sacuvajOglas.setOnClickListener {


            //za izmenu
            var txtProdajaDaNe: Int
            var txtIdKategorije by Delegates.notNull<Int>()
            var txtStanje by Delegates.notNull<Int>()
            var txtInterfon by Delegates.notNull<Int>()
            var txtInternet by Delegates.notNull<Int>()
            var txtTerasa by Delegates.notNull<Int>()
            var txtTelefon by Delegates.notNull<Int>()
            var txtGrejanje by Delegates.notNull<Int>()
            var txtKlima by Delegates.notNull<Int>()
            var txtKATV by Delegates.notNull<Int>()
            var txtParking by Delegates.notNull<Int>()
            var txtLift by Delegates.notNull<Int>()
            var txtVideoNadzor by Delegates.notNull<Int>()
            var txtNamestenost by Delegates.notNull<Int>()
            var txtGradnja by Delegates.notNull<Int>()

            txtIdKategorije = 1
            txtStanje = 1
            txtGradnja = 1
            txtNamestenost = 1

            if(adedit_prodaja.isChecked)
                txtProdajaDaNe = 1
            else
                txtProdajaDaNe = 0

            //TERASA
            if (adedit_terasa.isChecked)
                txtTerasa = 1
            else
                txtTerasa = 0

            //INTERNET
            if (adedit_internet.isChecked)
                txtInternet = 1
            else
                txtInternet = 0

            //TELEFON
            if (adedit_telefon.isChecked)
                txtTelefon = 1
            else
                txtTelefon = 0

            //GREJANJE
            if (adedit_grejanje.isChecked)
                txtGrejanje = 1
            else
                txtGrejanje = 0

            //INTERFON
            if (adedit_interfon.isChecked)
                txtInterfon = 1
            else
                txtInterfon = 0

            //KLIMA
            if (adedit_klima.isChecked)
                txtKlima = 1
            else
                txtKlima = 0

            //PARKING
            if (adedit_parking.isChecked)
                txtParking = 1
            else
                txtParking = 0

            //LIFT
            if (adedit_lift.isChecked)
                txtLift = 1
            else
                txtLift = 0

            //KATV
            if (adedit_katv.isChecked)
                txtKATV = 1
            else
                txtKATV = 0

            //VIDEO NADZOR
            if (adedit_video_nadzor.isChecked)
                txtVideoNadzor = 1
            else
                txtVideoNadzor = 0

            if (adedit_kategorija.selectedItem.toString().equals("Garsonjera"))
                txtIdKategorije = 1
            else if (adedit_kategorija.selectedItem.equals("Stan"))
                txtIdKategorije = 2
            else if (adedit_kategorija.selectedItem.equals("Kuća"))
                txtIdKategorije = 3
            else if (adedit_kategorija.selectedItem.equals("Poslovni prostor"))
                txtIdKategorije = 4
            else if (adedit_kategorija.selectedItem.equals("Vikendica"))
                txtIdKategorije = 5


            if (adedit_stanjeObjekta.selectedItem.equals("Izvorno"))
                txtStanje = 1
            else if (adedit_stanjeObjekta.selectedItem.equals("Renoviran"))
                txtStanje = 2
            else if (adedit_stanjeObjekta.selectedItem.equals("Potrebno renoviranje"))
                txtStanje = 3

            if (adedit_namestenost.selectedItem.equals("Nenamešten"))
                txtNamestenost = 1
            else if (adedit_namestenost.selectedItem.equals("Polunamešten"))
                txtNamestenost = 2
            else if (adedit_namestenost.selectedItem.equals("Namešten"))
                txtNamestenost = 3

            if (adedit_gradnja.selectedItem.equals("Stara gradnja"))
                txtGradnja = 1
            else if (adedit_gradnja.selectedItem.equals("Nova gradnja"))
                txtGradnja = 2





            izmenjeniOglas = Oglas(
                oglas.idOglasa,
                oglas.idVlasnika,
                oglas.idStana,
                adedit_naslov_naslov.text.toString(),
                adedit_opis.text.toString(),
                adedit_cena.text.toString().toInt(),
                null,
                txtProdajaDaNe,
                oglas.brojLajkova,
                oglas.brojPregleda
            )

            izmenjeniStan =  Stan(
                stan.idStana,
                stan.idVlasnika,
                adedit_povrsina.text.toString().toInt(),
                stan.grad,
                adedit_adresa.text.toString(),
                adedit_brojSoba.text.toString().toInt(),
                txtIdKategorije,
                txtStanje,
                txtNamestenost,
                txtGradnja,
                adedit_brojSpratova.text.toString().toInt(),
                txtTerasa,
                txtTelefon,
                txtInternet,
                txtKATV,
                txtInterfon,
                txtLift,
                txtKlima,
                txtGrejanje,
                txtParking,
                txtVideoNadzor
            )

            //RETROFIT S
            val repository = Repository()

            val viewModelFactory = MainViewModelFactory(repository)

            viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

            viewModel.izmeniOglas(izmenjeniOglas)
            viewModel.myResponse1.observe(
                this,
                androidx.lifecycle.Observer { response ->
                    if (response.isSuccessful()) {
                        Log.d("Main", response.body().toString())
                        Log.d("Main", response.code().toString())
                        Log.d("Main", response.message().toString())

                        Toast.makeText(
                            this,
                            "Promene su uspešno sačuvane za oglas " + oglas.naslovOglasa,
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Promene nisu sačuvane za oglas " + oglas.naslovOglasa,
                            Toast.LENGTH_LONG
                        ).show()
                        println("ERROR 592$3 CTRL F IZMENI OGLAS EDITOGLASAPG")
                    }
                })

            viewModel.izmeniStan(izmenjeniStan)
            viewModel.myResponse1.observe(
                this,
                androidx.lifecycle.Observer { response ->
                    if (response.isSuccessful()) {
                        Log.d("Main", response.body().toString())
                        Log.d("Main", response.code().toString())
                        Log.d("Main", response.message().toString())


                    } else {

                        println("ERROR 592$3 CTRL F IZMENI STAN EDITGOLASA")
                    }
                })
            postaviSlike(izmenjeniOglas.idOglasa)

            //val intent = Intent(this, OglasPage::class.java)
            //intent.putExtra("id", idOglasa)
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }

    }

    private fun postaviSlike(idOglasa: String) {
        println("JAJE : " )
        if(images != null)
        {
            println("JAJE 1")
            println("IAM" + images)
        }
        if(images!!.size > 0)
        {
            println("jaje 2")
        }
        if(images!!.size == 0)
        {
            println("jaje 3")
        }
        for (i in images!!) {
            val imageUri: Uri? = i
            val imageStream: InputStream? = imageUri?.let {
                this.getContentResolver()?.openInputStream(
                    it
                )
            }
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            val encodedImage: String = encodeImage(selectedImage)
            println("TAGE" + encodedImage)

            slikeOglasa.add(SlikaOglasa("b5b560c4-53e1-467a-953b-f50b6c39fcea",idOglasa,encodedImage))

        }



        //RETROFIT S
        val repository = Repository()

        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        println(" SLIKASKKA : " + slikeOglasa.toString())
        //izmena slika
        viewModel.izmeniSlikeOglasa(slikeOglasa)

        viewModel.myResponse3.observe(
            this,
            androidx.lifecycle.Observer { response ->
                if (response.isSuccessful) {
                    Log.d("Main", response.body().toString())
                    Log.d("Main", response.code().toString())
                    Log.d("Main", response.message().toString())
                } else {
                    //Toast.makeText(view.context, response.code(), Toast.LENGTH_SHORT).show()
                    println("ERROR 592$99 CTRL IMG OEDIT OGLASPG")
                }
            })
        //RETROFIT E
    }

    private fun encodeImage(bm: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


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

    private fun pickImagesIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        getResult.launch(intent)
    }

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {

            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data!!.clipData != null) {
                    val count = it.data!!.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = it.data!!.clipData!!.getItemAt(i).uri
                        images!!.add(imageUri)
                    }
                    adedit_slike.setImageURI(images!![0])
                    position = 0

                } else {
                    val imageUri = it.data!!.data
                    images!!.add(imageUri)
                    adedit_slike.setImageURI(imageUri)
                    position = 0
                }
            }

        }


}





