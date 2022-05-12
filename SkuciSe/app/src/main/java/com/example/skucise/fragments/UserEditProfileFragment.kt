package com.example.skucise.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.skucise.HomePage
import com.example.skucise.R
import com.example.skucise.User_profile_ad
import com.example.skucise.activity.ChangePassword
import com.example.skucise.activity.OglasPage
import com.example.skucise.activity.Validator
import com.example.skucise.model.SlikaKorisnika
import com.example.skucise.model.Vlasnik
import com.example.skucise.util.Constants
import com.example.skucise.util.MainViewModel
import com.example.skucise.util.MainViewModelFactory
import com.example.skucise.util.Repository
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.view.*
import kotlinx.android.synthetic.main.activity_oglas_page.*
import kotlinx.android.synthetic.main.edit_oglas_page.*
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


class UserEditProfileFragment  : Fragment() {
    private lateinit var viewModel: MainViewModel
    companion object {

        //izaberi sliku - kod
        private val IMAGE_PICK_CODE = 1000
        private const val SELECT_PHOTO = 12345
        var encodedImage: String? = null
        //premmission code
        private val PERMISSION_CODE = 1001
        lateinit var vlasnik: Vlasnik
        lateinit var vlasnikIzmenjen : Vlasnik
        var responseFlag :Boolean = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.activity_edit_profile, container, false)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(view.context)
        val usernameLogovanog = sharedPref.getString("Username", " ")

        var obj = JSONObject()
        var book = JSONObject()


        book = sendGetRequest("vlasnikUser/" + usernameLogovanog)

        vlasnik = Vlasnik(
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
        bookS = sendGetRequest("dajSlikuKorisnika/" + vlasnik.idVlasnika)
        if(responseFlag)
        {
            val decodedString: ByteArray = Base64.decode(bookS.get("slika").toString(), DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            view.profile_picture_id.setImageBitmap(decodedByte)
        }
        else
        {
            view.profile_picture_id.setBackgroundResource(R.drawable.ic_baseline_person_24)
        }

        view.izmeni_profilnu.setOnClickListener {
            /* OVO NE SME DA SE BRISE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions,PERMISSION_CODE)
                    pickImageFromGallery()// POTREBNO JE OBRISATI I ISPRAVITI GRESKU OKO ODBIJANJA OVE FUNKCIJE
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                //system os is < marshmallow
                pickImageFromGallery()
            }
*/
            pickImageFromGallery()
        }

        view.edit_username.setText(vlasnik.username.toString().uppercase())

        view.edit_sacuvaj_izmene_btn.isEnabled = false
        //IME I PREZIME
        view.edit_ime_id.setText(vlasnik.punoIme)
        view.edit_ime_id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (view.edit_ime_id.text.toString().isEmpty())
                    view.edit_sacuvaj_izmene_btn.isEnabled = true
                else
                    view.edit_sacuvaj_izmene_btn.isEnabled = Validator.isValidName(edit_ime_id)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


        //BROJ TELEFONA
        view.edit_br_telefona_id.setText(vlasnik.brojTelefona)
        view.edit_br_telefona_id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (view.edit_br_telefona_id.text.toString().isEmpty())
                    view.edit_sacuvaj_izmene_btn.isEnabled = true
                else
                    view.edit_sacuvaj_izmene_btn.isEnabled =
                        Validator.isValidPhone(edit_br_telefona_id)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        view.edit_izmeni_lozinku.setOnClickListener {
            val intent = Intent(context, ChangePassword::class.java)
            startActivity(intent)
        }


        view.edit_sacuvaj_izmene_btn.setOnClickListener {
            //RETROFIT
            //
            val repository = Repository()

            var ime = view.edit_ime_id.text.toString().trim()
            var broj = view.edit_br_telefona_id.text.toString().trim()
            vlasnikIzmenjen = Vlasnik(
                vlasnik.idVlasnika,
                vlasnik.username,
                ime,
                broj,
                vlasnik.brojOglasa,
                vlasnik.zbirSvihOcena,
                vlasnik.brojOcena,
                vlasnik.email,
                vlasnik.password
            )
            val viewModelFactory = MainViewModelFactory(repository)
            var viewModel: MainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

            //dodavanje Korisnika
            viewModel.izmeniVlasnika(vlasnikIzmenjen)

            viewModel.myResponse2.observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer { response ->
                    if (response.isSuccessful) {
                        Log.d("Main", response.body().toString())
                        Log.d("Main", response.code().toString())
                         Log.d("Main", response.message().toString())

                        /*if(response.body().toString().isEmpty())
                            Toast.makeText(view.context, "Korisnik sa tim korisničkim imenom/email-om već postoji.", Toast.LENGTH_SHORT).show()
*/
                            Toast.makeText(view.context, "Izmene su uspešno sačuvane!", Toast.LENGTH_SHORT).show()
                            println("PROVERA  > " + vlasnikIzmenjen.punoIme + " "+ vlasnikIzmenjen.brojTelefona)
                        var fr = getFragmentManager()?.beginTransaction()?.addToBackStack(null)
                        fr?.replace(R.id.fragment_container, UserProfile())
                        fr?.commit()
                    }
                        else{

                            Toast.makeText(view.context, "Izmene nisu uspešno izvršene, molimo pokušajte kasnije", Toast.LENGTH_SHORT).show()
                        }
                    }
                )


            //RETROFIT E



/*
           var fr = getFragmentManager()?.beginTransaction()?.addToBackStack(null)
            fr?.replace(R.id.fragment_container, UserProfile())
            fr?.commit()*/
        }



        return view
    }

    private fun pickImageFromGallery() {
        //intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(context, "Odbijen pristup galeriji", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            if (data != null) {
                profile_picture_id.setImageURI(data?.data)//profil

                val imageUri: Uri? = data.data


                val input = activity?.getContentResolver()?.openInputStream(imageUri!!)
                val image = BitmapFactory.decodeStream(input , null, null)

                // Encode image to base64 string
                val baos = ByteArrayOutputStream()
                if (image != null) {
                    image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                }
                var imageBytes = baos.toByteArray()
                val imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)



                var slikaKorisnika : SlikaKorisnika = SlikaKorisnika(vlasnik.idVlasnika, imageString)
                //RETROFIT S
                val repository = Repository()

                val viewModelFactory = MainViewModelFactory(repository)

                viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

                //dodavanje Stana
                viewModel.izmeniSlikuKorisnika(slikaKorisnika)

                viewModel.myResponse.observe(
                    viewLifecycleOwner,
                    androidx.lifecycle.Observer { response ->
                        if (response.isSuccessful) {
                            Log.d("Main", response.body().toString())
                            Log.d("Main", response.code().toString())
                            Log.d("Main", response.message().toString())
                        } else {
                            //Toast.makeText(view.context, response.code(), Toast.LENGTH_SHORT).show()
                            println("ERROR 592$99 CTRL G")
                        }
                    })


                //RETROFIT E

            }
        }
    }
    private fun encodeImage(bm: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        val imageString = Base64.encodeToString(b, Base64.DEFAULT)
        return imageString
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
            if (responseCode == 200) {
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

