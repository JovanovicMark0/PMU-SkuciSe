package com.example.skucise.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.StrictMode
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.skucise.HomePage
import com.example.skucise.R
import com.example.skucise.util.Constants.Companion.BASE_URL
import kotlinx.android.synthetic.main.login_tab_fragment.*
import kotlinx.android.synthetic.main.login_tab_fragment.view.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Login : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val view: View = inflater!!.inflate(R.layout.login_tab_fragment, container, false)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        //----------------------------

        view.forgot_pass.setOnClickListener {

            val intent = Intent(context, ForgotPassword::class.java)
            startActivity(intent)
        }

        view.login_button.setOnClickListener { view ->
            val username = username_id.text.toString().trim()
            val pass = password.text.toString().trim()
            println("KLIK")



            if(username.isEmpty())
            {

                username_id.setHint("Obavezno polje*")
                username_id.setHintTextColor(Color.RED)
            }
            if(pass.isEmpty())
            {
                password.setHint("Obavezno polje*")
                password.setHintTextColor(Color.RED)
            }
            if(!username.isEmpty() && !pass.isEmpty())
            {
                var res = StringBuffer()
                res = sendLoginRequest("login/"+username_id.text.toString() + "/"+password.text.toString())
                if(res.trim().equals("\"User is not existing in Database\""))
                {
                    username_id.setText("")
                    username_id.setHint("Korisnik ne postoji!")
                    username_id.setHintTextColor(Color.RED)
                }
                else if(res.trim().equals("\"Wrong Password\""))
                {
                    password.setText("")
                    password.setHint("Netačna lozinka!")
                    password.setHintTextColor(Color.RED)
                }
                else
                {
                    val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
                    val editor = sharedPref.edit()
                    editor.putBoolean("Registered", true)
                    editor.putString("Username", username)
                    editor.putString("Password", pass)
                    editor.apply()

                    val intent = Intent(context, HomePage::class.java)
                    startActivity(intent)
                }
                println(res)
                println("\"User is not existing in Database\"")

            }
        }


        return view
    }


    fun  sendLoginRequest(requestText :String) :StringBuffer{


        val mURL = URL(BASE_URL + "api/"+requestText)
        val response = StringBuffer()
        with(mURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "GET"

            println("URL : $url")
            println("Response Code : $responseCode")

            //UPIT
            if(responseCode == 200)
            {
                BufferedReader(InputStreamReader(inputStream)).use {
                    var inputLine = it.readLine()

                    while (inputLine != null) {
                        response.append(inputLine)

                        inputLine = it.readLine()
                    }
                    it.close()
                    println("RESPONSE > $response")
                }
            }
            else
            {
                response.append("GRESKA")
            }
        }
        return response
    }
}
