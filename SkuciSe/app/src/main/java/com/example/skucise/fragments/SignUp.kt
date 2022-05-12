package com.example.skucise.fragments

import com.example.skucise.activity.Validator.Companion.isValidEmail
import com.example.skucise.activity.Validator.Companion.isValidName
import com.example.skucise.activity.Validator.Companion.isValidPassword
import com.example.skucise.activity.Validator.Companion.isValidPhone
import com.example.skucise.activity.Validator.Companion.isValidUsername
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.skucise.HomePage
import com.example.skucise.MainActivity
import com.example.skucise.R
import com.example.skucise.activity.Login
import com.example.skucise.model.Vlasnik
import com.example.skucise.util.MainViewModel
import com.example.skucise.util.MainViewModelFactory
import com.example.skucise.util.Repository
import kotlinx.android.synthetic.main.signup_tab_fragment.*
import kotlinx.android.synthetic.main.signup_tab_fragment.view.*

class SignUp : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View =  inflater.inflate(R.layout.signup_tab_fragment, container, false)
        view.reg_button.isEnabled = false;



        //IME I PREZIME
        view.reg_ime.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(isValidName(view.reg_ime)) {
                    if (isValidPhone(view.reg_broj)
                        && isValidEmail(view.reg_email)
                        && isValidUsername(view.reg_username)
                        && isValidPassword(view.password_reg)
                        && view.conf_password.getText().toString()
                            .equals(view.password_reg.text.toString())
                    )
                        reg_button.isEnabled = true
                }
                else
                {
                    reg_button.isEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })



        //BROJ TELEFONA
        view.reg_broj.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(isValidPhone(view.reg_broj)){
                    if (isValidName(view.reg_ime)
                        && isValidEmail(view.reg_email)
                        && isValidUsername(view.reg_username)
                        && isValidPassword(view.password_reg)
                        && view.conf_password.getText().toString()
                            .equals(view.password_reg.text.toString())
                    )
                        reg_button.isEnabled = true
                }
                else
                {
                    reg_button.isEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        //EMAIl
        view.reg_email.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isValidEmail(view.reg_email)){
                    if (isValidName(view.reg_ime)
                        && isValidPhone(view.reg_broj)
                        && isValidUsername(view.reg_username)
                        && isValidPassword(view.password_reg)
                        && view.conf_password.getText().toString()
                            .equals(view.password_reg.text.toString())
                    )
                        reg_button.isEnabled = true
                }
                else
                {
                    reg_button.isEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        //USERNAME
        view.reg_username.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isValidUsername(view.reg_username)){
                    if (isValidName(view.reg_ime)
                        && isValidPhone(view.reg_broj)
                        && isValidEmail(view.reg_email)
                        && isValidPassword(view.password_reg)
                        && view.conf_password.getText().toString()
                            .equals(view.password_reg.text.toString())
                    )
                        reg_button.isEnabled = true
                }
                else
                {
                    reg_button.isEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        //PASSWORDD
        view.password_reg.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isValidPassword(view.password_reg)){
                    if (isValidName(view.reg_ime)
                        && isValidPhone(view.reg_broj)
                        && isValidEmail(view.reg_email)
                        && isValidUsername(view.reg_username)
                        && view.conf_password.getText().toString()
                            .equals(view.password_reg.text.toString())
                    )
                        reg_button.isEnabled = true
                }
                else
                {
                    reg_button.isEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        //PASSWORDD_CONF
        view.conf_password.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (view.conf_password.getText().toString().equals(view.password_reg.text.toString())){
                    if (isValidName(view.reg_ime)
                        && isValidPhone(view.reg_broj)
                        && isValidEmail(view.reg_email)
                        && isValidUsername(view.reg_username)
                        && isValidPassword(view.password_reg)
                    )
                        reg_button.isEnabled = true
                }
                else
                {
                    reg_button.isEnabled = false
                    view.conf_password.setError("Lozinke se ne poklapaju!")
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })


        view.reg_button.setOnClickListener{view->
            val punoIme = reg_ime.text.toString().trim()
            val brojTelefona = reg_broj.text.toString().trim()
            val email = reg_email.text.toString().trim()
            val pass = password_reg.text.toString().trim()
            val username = reg_username.text.toString().trim()
            val vlasnik:Vlasnik = Vlasnik("b5b560c4-53e1-467a-953b-f50b6c39fcea",username,punoIme,brojTelefona,0,0,0,email, pass)
            //RETROFIT S
            val repository = Repository()

            val viewModelFactory = MainViewModelFactory(repository)
            var viewModel: MainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

            //dodavanje Korisnika
            viewModel.dodajKorisnika(vlasnik)

            viewModel.myResponse2.observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer { response ->
                    if (response.isSuccessful) {
                        //Log.d("Main", response.body().toString())
                        Log.d("Main", response.code().toString())
                       // Log.d("Main", response.message().toString())

                        /*if(response.body().toString().isEmpty())
                            Toast.makeText(view.context, "Korisnik sa tim korisničkim imenom/email-om već postoji.", Toast.LENGTH_SHORT).show()
*/
                        if(response.code() == 200)
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
                        else{
                            Toast.makeText(view.context, "Korisnik sa tim korisničkim imenom/email-om već postoji.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        //Toast.makeText(view.context, response.code(), Toast.LENGTH_SHORT).show()
                        println("ERROR 600$5")
                    }
                })


            //RETROFIT E
        }

        return view
    }

}