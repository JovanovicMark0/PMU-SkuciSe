package com.example.skucise.fragments

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import com.example.skucise.HomePage
import com.example.skucise.R
import com.example.skucise.activity.OglasPage
import com.example.skucise.model.Oglas
import com.example.skucise.model.SlikaKorisnika
import com.example.skucise.model.SlikaOglasa
import com.example.skucise.model.Stan
import com.example.skucise.util.Constants
import com.example.skucise.util.MainViewModel
import com.example.skucise.util.MainViewModelFactory
import com.example.skucise.util.Repository
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import kotlinx.android.synthetic.main.login_tab_fragment.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import kotlin.collections.ArrayList
import kotlin.properties.Delegates
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.MultipartBody.Part.Companion.create
import okhttp3.RequestBody


import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Part
import java.io.*


class Add : Fragment() {
    private lateinit var viewModel: MainViewModel
    var slikeOglasa = mutableListOf<SlikaOglasa>()
    lateinit var idVlasnika: String //IZ SHARED PREFFS
    lateinit var idStana: String //KADA UNESENMO STAN ONDA UPISEMO ID STANA KOJI SMO UNELI KAKO BI UNELI OGLAS
    lateinit var idOglasa: String //KADA UNESENMO STAN ONDA UPISEMO ID OGLASA KOJI SMO UNELI KAKO BI UNELI SLIKE
    lateinit var stan: Stan
    lateinit var oglas: Oglas
    var txtProdajaDaNe: Int = 0
    var txtTipStana by Delegates.notNull<Int>()
    lateinit var txtNaslov: String
    lateinit var txtOpis: String
    lateinit var txtGrad: String
    lateinit var txtAdresa: String
    var txtCena by Delegates.notNull<Int>()
    var txtNamestenost by Delegates.notNull<Int>()
    var txtStanje by Delegates.notNull<Int>()
    var txtGradnja by Delegates.notNull<Int>()
    var txtPovrsina by Delegates.notNull<Int>()
    var txtBrSpratova by Delegates.notNull<Int>()
    var txtBrSoba by Delegates.notNull<Int>()
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

    var slikeFlag: Int = 0 //Ako j e1 onda su slike vec uploadovane i zove se edit

    private var images: ArrayList<Uri?>? = null
    private var position = 0
    lateinit var context1: Context
    private val PICK_IMAGES_CODE = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_add, container, false)
        context1 = view.context
        val myColorStateList = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_pressed), intArrayOf()), intArrayOf(
                Color.RED,
                Color.BLUE
            )
        )

        view.radioButton2.isChecked = true
        view.buttonDodajOglas.setOnClickListener { view ->
            var flag = 1;
            //PROVERA DA LI SU UNETI SVI PODACI
            //NASLOV
            if (naslov.text.toString().isEmpty()) {
                naslov_layout.setHint("Naslov je obavezan!")
                naslov_layout.defaultHintTextColor = generateColorStateList("#FF3333")
                flag = 0;
            } else {
                naslov_layout.setHint("Naslov oglasa")
                naslov_layout.defaultHintTextColor = generateColorStateList("#808080")
            }

            //OPIS
            if (opis.text.toString().isEmpty()) {
                opis_layout.setHint("Opis je obavezan!")
                opis_layout.defaultHintTextColor = generateColorStateList("#FF3333")
                flag = 0;
            } else {
                opis_layout.setHint("Opis oglasa")
                opis_layout.defaultHintTextColor = generateColorStateList("#808080")
            }

            //CENA
            if (cena.text.toString().isEmpty()) {
                cena_layout.setHint("Cena je obavezna!")
                cena_layout.defaultHintTextColor = generateColorStateList("#FF3333")
                flag = 0;
            } else if (!cena.text.toString().isDigitsOnly()) {
                cena_layout.setHint("Cena mora biti broj!")
                cena_layout.defaultHintTextColor = generateColorStateList("#FF3333")
                flag = 0
            } else {
                cena_layout.setHint("Cena")
                cena_layout.defaultHintTextColor = generateColorStateList("#808080")
            }

            //POVRSINA
            if (!povrsina.text.toString().isEmpty() && !povrsina.text.toString().isDigitsOnly()) {
                povrsina_layout.setHint("Površina!")
                povrsina_layout.defaultHintTextColor = generateColorStateList("#FF3333")
                flag = 0
            } else {
                povrsina_layout.setHint("Površina")
                povrsina_layout.defaultHintTextColor = generateColorStateList("#808080")
            }

            //Broj spratova
            if (!brojSpratova.text.toString().isEmpty() && !brojSpratova.text.toString()
                    .isDigitsOnly()
            ) {
                spratovi_layout.setHint("Spratnost!")
                spratovi_layout.defaultHintTextColor = generateColorStateList("#FF3333")
                flag = 0
            } else {
                spratovi_layout.setHint("Broj spratova")
                spratovi_layout.defaultHintTextColor = generateColorStateList("#808080")
            }

            //Broj soba
            if (!brojSoba.text.toString().isEmpty() && !brojSoba.text.toString().isDigitsOnly()) {
                sobe_layout.setHint("Broj soba!")
                sobe_layout.defaultHintTextColor = generateColorStateList("#FF3333")
                flag = 0
            } else {
                sobe_layout.setHint("Broj soba")
                sobe_layout.defaultHintTextColor = generateColorStateList("#808080")
            }
            //KRAJ PROVERA  - - AKTIVNOST DODAVANJE OGLASA
            //AKO NIJE PROSAO PROVERE
            if (flag == 0) {
                textView2.text =
                    "Uneti podaci nisu ispravni,   molimo proverite ispravnost unetih podataka."
                textView2.setTextColor(Color.RED)

                scrollViewAdd.scrollTo(0, scrollViewAdd.top)
            }
            //AKTIVNOST AKO PRODJE PROVERU IZNAD
            else {
                //da dobijemo id vlasnika
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(view.context)
                //USERNAME LOGOVANE OSOBE KOJA KACI OGLAS
                val usernameLogovanog = sharedPref.getString("Username", " ")
                sendGetRequest("vlasnikUser/" + usernameLogovanog)

                //PRODAJADANE
                if (radioButton.isChecked) {
                    txtProdajaDaNe = 1;
                } else {
                    txtProdajaDaNe = 0;
                }
                //TIP STANA
                when (kategorija.selectedItemPosition) {
                    1 -> txtTipStana = 1
                    2 -> txtTipStana = 2
                    3 -> txtTipStana = 3
                    4 -> txtTipStana = 4
                    5 -> txtTipStana = 5
                    else -> {
                        txtTipStana = 1
                    }
                }
                //NASLOV
                txtNaslov = naslov.text.toString().trim()

                //OPIS
                txtOpis = opis.text.toString().trim()

                //GRAD
                txtGrad = grad.selectedItem.toString().trim()

                //ADRESA
                txtAdresa = adresa.text.toString().trim()

                //CENA
                txtCena = (cena.text.toString().trim()).toInt()

                //NAMESTENOST
                when (namestenost.selectedItemPosition) {
                    1 -> txtNamestenost = 1
                    2 -> txtNamestenost = 2
                    3 -> txtNamestenost = 3
                    else -> txtNamestenost = 1
                }
                //STANJE OBJEKTA
                when (stanjeObjekta.selectedItemPosition) {
                    1 -> txtStanje = 1
                    2 -> txtStanje = 2
                    3 -> txtStanje = 3
                    else -> txtStanje = 1
                }
                //gradnja
                when (gradnja.selectedItemPosition) {
                    1 -> txtGradnja = 1
                    2 -> txtGradnja = 2
                    else -> txtGradnja = 1
                }

                //POVRSINA
                if (povrsina.text.toString().isEmpty()) {
                    txtPovrsina = 0;
                } else {
                    txtPovrsina = (povrsina.text.toString().trim()).toInt()
                }

                //Br spratova
                if (brojSpratova.text.toString().isEmpty()) {
                    txtBrSpratova = 0;
                } else {
                    txtBrSpratova = (brojSpratova.text.toString().trim()).toInt()
                }

                //br soba
                if (brojSoba.text.toString().isEmpty()) {
                    txtBrSoba = 0;
                } else {
                    txtBrSoba = (brojSoba.text.toString().trim()).toInt()
                }

                //TERASA
                if (checkBox3.isChecked) {
                    txtTerasa = 1
                } else {
                    txtTerasa = 0
                }

                //INTERNET
                if (checkBox4.isChecked) {
                    txtInternet = 1
                } else {
                    txtInternet = 0
                }

                //TELEFON
                if (checkBox5.isChecked) {
                    txtTelefon = 1
                } else {
                    txtTelefon = 0
                }

                //GREJANJE
                if (checkBox9.isChecked) {
                    txtGrejanje = 1
                } else {
                    txtGrejanje = 0
                }

                //INTERFON
                if (checkBox.isChecked) {
                    txtInterfon = 1
                } else {
                    txtInterfon = 0
                }

                //KLIMA
                if (checkBox8.isChecked) {
                    txtKlima = 1
                } else {
                    txtKlima = 0
                }

                //PARKING
                if (checkBox10.isChecked) {
                    txtParking = 1
                } else {
                    txtParking = 0
                }

                //LIFT
                if (checkBox7.isChecked) {
                    txtLift = 1
                } else {
                    txtLift = 0
                }

                //KATV
                if (checkBox6.isChecked) {
                    txtKATV = 1
                } else {
                    txtKATV = 0
                }

                //VIDEO NADZOR
                if (checkBox2.isChecked) {
                    txtVideoNadzor = 1
                } else {
                    txtVideoNadzor = 0
                }


                stan = Stan(
                    "b5b560c4-53e1-467a-953b-f50b6c39fcea",
                    idVlasnika,
                    txtPovrsina,
                    txtGrad,
                    txtAdresa,
                    txtBrSoba,
                    txtTipStana,
                    txtStanje,
                    txtNamestenost,
                    txtGradnja,
                    txtBrSpratova,
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

                //dodavanje Stana
                viewModel.pushPost(stan)

                viewModel.myResponse.observe(
                    viewLifecycleOwner,
                    androidx.lifecycle.Observer { response ->
                        if (response.isSuccessful) {
                            Log.d("Main", response.body().toString())
                            Log.d("Main", response.code().toString())
                            Log.d("Main", response.message().toString())

                            idStana = response.body()?.idStana.toString()
                            postaviOglas(idStana)
                        } else {
                            //Toast.makeText(view.context, response.code(), Toast.LENGTH_SHORT).show()
                            println("ERROR 592$3 CTRL F")
                        }
                    })


                //RETROFIT E

            }
        }//onclick ENDS

        images = ArrayList()

        view.slike.setFactory { ImageView(view.context) }

        view.slike.setOnClickListener {
            pickImagesIntent()
            view.slike.setBackgroundResource(R.drawable.ic_baseline_check_box_outline_blank_24)

        }

        view.dugmeSledecaSlika.setOnClickListener {
            if (position < images!!.size - 1) {
                position++
                view.slike.setImageURI(images!![position])
            } else {
                Toast.makeText(view.context, "Nema više slika...", Toast.LENGTH_SHORT).show()
            }
        }

        view.dugmePrethodnaSlika.setOnClickListener {
            if (position > 0) {
                position--
                view.slike.setImageURI(images!![position])
            } else {
                Toast.makeText(view.context, "Nema više slika...", Toast.LENGTH_SHORT).show()
            }
        }


        return view
    }

    private fun encodeImage(bm: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun generateColorStateList(
        color: String

    ): ColorStateList {
        val states = arrayOf(
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(-android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_active),
            intArrayOf(-android.R.attr.state_active),
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf(-android.R.attr.state_pressed),
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(-android.R.attr.state_focused)
        )
        val colors = intArrayOf(
            Color.parseColor(color), // enabled
            Color.parseColor(color), // disabled
            Color.parseColor(color), // checked
            Color.parseColor(color), // unchecked
            Color.parseColor(color), // active
            Color.parseColor(color), // inactive
            Color.parseColor(color), // pressed
            Color.parseColor(color), // focused
            Color.parseColor(color),
            Color.parseColor(color)
        )
        return ColorStateList(states, colors)
    }

    private fun postaviOglas(idStana: String) {
        //DODAVANJE OGLASA
        oglas = Oglas(
            "b5b560c4-53e1-467a-953b-f50b6c39fcea",
            idVlasnika,
            idStana,
            txtNaslov,
            txtOpis,
            txtCena,
            null,
            txtProdajaDaNe,
            0,
            0
        )
        println("OGLAOLA> > > " + oglas)
        viewModel.pushOglas(oglas)
        viewModel.myResponse1.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                if (response.isSuccessful()) {
                    Log.d("Main", response.body().toString())
                    Log.d("Main", response.code().toString())
                    Log.d("Main", response.message().toString())

                    if (images!!.size != 0) {
                        idOglasa = response.body().toString()
                        postaviSlike(idOglasa)
                    }
                    else
                    {
                        val intent = Intent(view?.context, OglasPage::class.java)
                        intent.putExtra("id" , response.body().toString())
                        startActivity(intent)
                    }
                } else {
                    //Toast.makeText(view.context, response.code(), Toast.LENGTH_SHORT).show()
                    println("ERROR 592$3 CTRL F")
                }
            })

        println("PUSHOVAN OGLAS")

        val intent = Intent(context1, HomePage::class.java)
        startActivity(intent)

    }

    private fun postaviSlike(idOglasa: String) {
        for (i in images!!) {
            val imageUri: Uri? = i
            val imageStream: InputStream? = imageUri?.let {
                context?.getContentResolver()?.openInputStream(
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

            //dodavanje Stana
            viewModel.dodajSlikeOglasa(slikeOglasa)

            viewModel.myResponse.observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer { response ->
                    if (response.isSuccessful) {
                        Log.d("Main", response.body().toString())
                        Log.d("Main", response.code().toString())
                        Log.d("Main", response.message().toString())

                    } else {
                        //Toast.makeText(view.context, response.code(), Toast.LENGTH_SHORT).show()
                        println("ERROR 592$99 CTRL IMG OGLAS ADDPG")
                    }
                })
            //RETROFIT E

        val intent = Intent(view?.context, OglasPage::class.java)
        intent.putExtra("id" , idOglasa)
        startActivity(intent)
        }



    //GET REQ
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendGetRequest(requestText: String): StringBuffer {

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


    //FUNKCIJA KOJA RACUNA x DANA OD DANAS KAKO BI OBRISALI OGLAS
    fun getDateFromNow(): LocalDateTime {
        var today: LocalDateTime = LocalDateTime.now()
        var time: LocalDateTime = today.plusDays(15)
        return time
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
                    slike.setImageURI(images!![0])
                    position = 0

                } else {
                    val imageUri = it.data!!.data
                    slike.setImageURI(imageUri)
                    images!!.add(imageUri)
                    position = 0
                }
            }

        }

}