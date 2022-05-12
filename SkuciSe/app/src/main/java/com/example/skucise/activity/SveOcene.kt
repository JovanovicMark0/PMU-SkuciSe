package com.example.skucise.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.skucise.R
import com.example.skucise.fragments.*
import com.example.skucise.fragments.Add
import com.example.skucise.util.MainViewModel
import com.example.skucise.util.MainViewModelFactory
import com.example.skucise.util.Repository
import kotlinx.android.synthetic.main.activity_main.*

class SveOcene : AppCompatActivity() {

    var notifyFlag : Boolean = false
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val korisnik: String? = intent.getStringExtra("usernameVlasnika")
        val korisnikId: String? = intent.getStringExtra("idVlasnika")



        setContentView(R.layout.activity_main)

        replaceFragment(SveOceneF(korisnik,korisnikId))

        chipNavigationBar.setOnItemSelectedListener {
                id -> when(id)
        {

            R.id.navbar_new_add -> {
                replaceFragment(Add())
                if (korisnikId != null) {
                    proveriNotifikacije(korisnikId)
                }
            }
            R.id.navbar_profile -> {
                replaceFragment(UserProfile())
                if (korisnikId != null) {
                    proveriNotifikacije(korisnikId)
                }
            }
            R.id.navbar_home -> {
                replaceFragment(Home())
                if (korisnikId != null) {
                    proveriNotifikacije(korisnikId)
                }
            }
            R.id.navbar_messages -> {
                replaceFragment(Notifications())
                notifyFlag = false
                chipNavigationBar.setMenuResource(R.menu.bottom_menu)
                chipNavigationBar.setBackgroundColor(Color.parseColor("#ffffff"))}
            R.id.navbar_like -> {
                replaceFragment(Like())
                if (korisnikId != null) {
                    proveriNotifikacije(korisnikId)
                }
            }

        }
        }

    }

    private fun replaceFragment(fragment: Fragment)
    {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
    fun proveriNotifikacije(idKorisnika:String)
    {
        if(notifyFlag == false) {
            //RETROFIT S
            val repository = Repository()

            val viewModelFactory = MainViewModelFactory(repository)
            var viewModel: MainViewModel =
                ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

            //dodavanje Korisnika
            viewModel.imaLiNotifikacija(idKorisnika)
            notifyFlag = false
            viewModel.myResponse4.observe(
                this,
                androidx.lifecycle.Observer { response ->
                    if (response.isSuccessful) {
                        Log.d("Main", response.body().toString())
                        Log.d("Main", response.code().toString())
                        Log.d("Main", response.message().toString())

                        if (response.code() == 200) {
                            if (response.body() == true) {
                                chipNavigationBar.setMenuResource(R.menu.bottom_menu_notify)
                                chipNavigationBar.setBackgroundColor(Color.parseColor("#FCB9AD"))
                                notifyFlag = true
                            } else {
                                chipNavigationBar.setMenuResource(R.menu.bottom_menu)
                                chipNavigationBar.setBackgroundColor(Color.parseColor("#ffffff"))
                                notifyFlag = false
                            }
                        }

                    } else {
                        //Toast.makeText(view.context, response.code(), Toast.LENGTH_SHORT).show()
                        println("ERROR 600212$5")
                        notifyFlag = false
                    }
                })
        }
    }
}
