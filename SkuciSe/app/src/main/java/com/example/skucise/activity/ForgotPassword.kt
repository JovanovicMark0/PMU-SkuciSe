package com.example.skucise.activity

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.skucise.HomePage
import com.example.skucise.R
import com.example.skucise.activity.Validator.Companion.isValidPassword
import com.example.skucise.model.Vlasnik
import com.example.skucise.util.MainViewModel
import com.example.skucise.util.MainViewModelFactory
import com.example.skucise.util.Repository
import kotlinx.android.synthetic.main.activity_forgoten_pass.*

class ForgotPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgoten_pass)

        val forgotPassRequestBtn = findViewById<Button>(R.id.resetPassReq)
        forgotPassRequestBtn.isEnabled = false

        //Username
        req_username.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (req_username.text.toString().isNotEmpty()){
                    forgotPassRequestBtn.isEnabled =
                        isValidPassword(req_pass) && req_email.text.toString().isNotEmpty()
                }
                else
                {
                    forgotPassRequestBtn.isEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        //EMAIl
        req_email.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (req_email.text.toString().isNotEmpty()){
                    forgotPassRequestBtn.isEnabled =
                        isValidPassword(req_pass) && req_username.text.toString().isNotEmpty()
                }
                else
                {
                    forgotPassRequestBtn.isEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        //PASSWORDD
        req_pass.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isValidPassword(req_pass)){
                    forgotPassRequestBtn.isEnabled =
                        req_username.text.toString().isNotEmpty() && req_email.text.toString().isNotEmpty()
                }
                else
                {
                    forgotPassRequestBtn.isEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })




        forgotPassRequestBtn.setOnClickListener{
            val username = findViewById<EditText>(R.id.req_username)
            val email = findViewById<EditText>(R.id.reg_email)
            val pass = findViewById<EditText>(R.id.req_pass)

            var vlasnik = Vlasnik("0152c694-2cc6-48b0-be9f-d0ac36351219",req_username.text.toString().trim(),"A","0",0,0,0,req_email.text.toString().trim(),req_pass.text.toString().trim())
            //RETROFIT S
            val repository = Repository()

            val viewModelFactory = MainViewModelFactory(repository)
            var viewModel: MainViewModel = ViewModelProvider(this, viewModelFactory).get(
                MainViewModel::class.java)

            //dodavanje Korisnika
            viewModel.zaboravljenaLozinka(vlasnik)

            viewModel.myResponse4.observe(
                this,
                androidx.lifecycle.Observer { response ->
                    if (response.isSuccessful) {
                        Log.d("Main", response.body().toString())
                        Log.d("Main", response.code().toString())
                         Log.d("Main", response.message().toString())

                        /*if(response.body().toString().isEmpty())
                            Toast.makeText(view.context, "Korisnik sa tim korisničkim imenom/email-om već postoji.", Toast.LENGTH_SHORT).show()
*/
                        if(response.code() == 200)
                        {
                            Toast.makeText(this.applicationContext, "Lozinka je uspešno izmenjena!", Toast.LENGTH_SHORT).show()
                            val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
                            val usernameLogovanog = sharedPref.getString("Username", "")
                            val editor = sharedPref.edit()
                            editor.clear()
                            editor.commit()
                            editor.putBoolean("Registered", true)
                            editor.putString("Username", usernameLogovanog)
                            editor.putString("Password", req_pass.text.toString())
                            editor.apply()
                            val intent = Intent( this, HomePage::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this, "Korisnik sa tim korisničkim imenom/email-om ne postoji.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        //Toast.makeText(view.context, response.code(), Toast.LENGTH_SHORT).show()
                        println("ERROR 600$5")
                    }
                })


            //RETROFIT E

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}