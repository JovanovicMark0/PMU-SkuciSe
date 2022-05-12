package com.example.skucise

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.example.skucise.fragments.*
import com.example.skucise.fragments.Add
import com.example.skucise.model.Oglas
import com.example.skucise.util.Constants
import com.example.skucise.util.Constants.Companion.BASE_URL
import com.example.skucise.util.MainViewModel
import com.example.skucise.util.MainViewModelFactory
import com.example.skucise.util.Repository
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import java.util.*

class HomePage : AppCompatActivity() {

    private val addFragment = com.example.skucise.fragments.Add()

    private companion object{
        private const val TAG = "HomePage"
        var notifyFlag : Boolean = false
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        //----------------------------

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val usernameLogovanog = sharedPref.getString("Username", " ")
        var book : JSONObject
        book = sendGetRequest("vlasnikUser/"+usernameLogovanog) //OVDEEEEEE
        var ulogovaniID = book.get("idVlasnika").toString()
        proveriNotifikacije(ulogovaniID)


        replaceFragment(Home())
        chipNavigationBar.setOnItemSelectedListener {
                id -> when(id)
        {

            R.id.navbar_new_add -> {
                replaceFragment(Add())
                proveriNotifikacije(ulogovaniID)
            }
            R.id.navbar_profile -> {
                replaceFragment(UserProfile())
                proveriNotifikacije(ulogovaniID)
            }
            R.id.navbar_home -> {
                replaceFragment(Home())
                proveriNotifikacije(ulogovaniID)
            }
            R.id.navbar_messages -> {
                replaceFragment(Notifications())
                notifyFlag = false
                chipNavigationBar.setMenuResource(R.menu.bottom_menu)
                chipNavigationBar.setBackgroundColor(Color.parseColor("#ffffff"))}
            R.id.navbar_like -> {
                replaceFragment(Like())
                proveriNotifikacije(ulogovaniID)
            }

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

    override fun onBackPressed() {
        if(chipNavigationBar.getSelectedItemId()!=R.id.navbar_home)
        {
            chipNavigationBar.setItemSelected(R.id.navbar_home)
        }
        else
        {
            super.onBackPressed()
        }

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

    //RETROFIT E
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
}
