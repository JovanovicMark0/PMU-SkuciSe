package com.example.skucise

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.skucise.fragments.*
import com.example.skucise.fragments.Add
import kotlinx.android.synthetic.main.activity_main.*

class OceniKorisnika : AppCompatActivity() {

    private val addFragment = com.example.skucise.fragments.Add()

    private companion object{
        private const val TAG = "OceniKorisnika"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var korisnik: String? = intent.getStringExtra("usernameVlasnika")
        var naslov: String? = intent.getStringExtra("naslovOglasa")

        setContentView(R.layout.activity_main)

        replaceFragment(OceniKorisnikaFragment(korisnik,naslov))

        chipNavigationBar.setOnItemSelectedListener {
                id -> when(id)
        {

            R.id.navbar_new_add -> replaceFragment(Add())
            R.id.navbar_profile -> replaceFragment(UserProfile())
            R.id.navbar_home -> replaceFragment(Home())
            R.id.navbar_messages -> replaceFragment(Notifications())
            R.id.navbar_like -> replaceFragment(Like())

        }
        }

    }

    private fun replaceFragment(fragment: Fragment)
    {
        if(fragment!=null)
        {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

}
