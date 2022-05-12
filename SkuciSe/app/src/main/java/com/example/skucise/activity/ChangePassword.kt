package com.example.skucise.activity

import android.content.Intent
import android.graphics.Color
import com.example.skucise.activity.Validator.Companion.isValidPassword
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.skucise.HomePage
import com.example.skucise.R
import com.example.skucise.util.Constants
import kotlinx.android.synthetic.main.activity_change_pass.*
import kotlinx.android.synthetic.main.login_tab_fragment.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ChangePassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pass)



        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val usernamePass = sharedPref.getString("Username","")

        submitChangedPass.isEnabled = false



        //NOVA LOZINKA
        newPass.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                submitChangedPass.isEnabled = isValidPassword(newPass)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        //POTVRDA NOVE LOZINKE
        newPassVerify.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (newPass.text.toString().equals(newPassVerify.text.toString())){
                    if( isValidPassword(newPass))
                        submitChangedPass.isEnabled = true
                }
                else
                {
                    submitChangedPass.isEnabled = false
                    newPassVerify.setError("Lozinke se ne poklapaju!")
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        submitChangedPass.setOnClickListener{
           var res = sendLoginRequest("izmeniSifru/${usernamePass}/${currentPass.text.toString()}/${newPass.text.toString()}")
            println(" JAJE VELIKO > " +res)
            if(res.trim().equals("\"IZMENJENA SIFRA\""))
            {
                Toast.makeText(this.applicationContext, "Lozinka je uspešno sačuvana!", Toast.LENGTH_SHORT).show()
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
                val usernameLogovanog = sharedPref.getString("Username", "")
                val editor = sharedPref.edit()
                editor.clear()
                editor.commit()
                editor.putBoolean("Registered", true)
                editor.putString("Username", usernameLogovanog)
                editor.putString("Password", newPass.text.toString())
                editor.apply()
                val intent = Intent( this, HomePage::class.java)
                startActivity(intent)
            }
            else if(res.trim().equals("\"NIJE IZMENJENA SIFRA\""))
            {
                Toast.makeText(this.applicationContext, "Greška, pokušajte kasnije!", Toast.LENGTH_SHORT).show()
            }
            else if(res.trim().equals("\"Wrong Password\""))
            {
                Toast.makeText(this.applicationContext, "Pogrešna trenutna lozinka!", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(this.applicationContext, "Nepostojeći korisnik!", Toast.LENGTH_SHORT).show()
            }
            println(res)



        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun  sendLoginRequest(requestText :String) :StringBuffer{


        val mURL = URL(Constants.BASE_URL + "api/"+requestText)
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