package com.example.skucise.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.core.widget.doOnTextChanged
import com.example.skucise.R
import com.example.skucise.activity.Validator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.view.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import kotlinx.android.synthetic.main.fragment_filteri.view.*
import kotlinx.android.synthetic.main.fragment_filteri.view.povrsina_od


class Filteri : Fragment() {

    companion object {
        var flagProdaja = 0
        var flagIzdavanje = 0
        var grad = ""
        var struktura_stana = ""
        var namestenost = ""
        var stanje = ""
        var gradnja = ""
        var povrsina_od = 0
        var povrsina_do = 0
        var cena_od = 0
        var cena_do = 0
        var broj_soba = 0
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view : View = inflater!!.inflate(R.layout.fragment_filteri, container, false)

        view.button_prodaja.setOnClickListener {
            flagProdaja ++
            if(flagProdaja%2!=0)
            {
                view.button_prodaja.setBackgroundColor(R.color.new_add)
            }
            else
            {
                view.button_prodaja.setBackgroundColor(Color.GRAY)
            }

        }

        view.button_izdavanje.setOnClickListener {
            flagIzdavanje ++
            if(flagIzdavanje%2!=0)
            {
                view.button_izdavanje.setBackgroundColor(R.color.new_add)
            }
            else
            {
                view.button_izdavanje.setBackgroundColor(Color.GRAY)
            }

        }
        var spinner = view.filter_gradovi
        spinner?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                grad = "Izaberi grad"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                grad = spinner.selectedItem.toString()

            }

        }

        var spinner2 = view.filter_kategorija
        spinner2?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                struktura_stana = ""
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                struktura_stana = spinner2.selectedItem.toString()

            }

        }
        var spinner3 = view.filter_namestenost
        spinner3?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                namestenost = ""
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                namestenost = spinner3.selectedItem.toString()

            }

        }
        var spinner4 = view.filter_stanjeObjekta
        spinner4?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                stanje = ""
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                stanje = spinner4.selectedItem.toString()

            }

        }
        var spinner5 = view.filter_gradnja
        spinner5?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                gradnja = ""
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                gradnja = spinner5.selectedItem.toString()

            }

        }

        view.povrsina_od.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var p1 = view.povrsina_od_layout.editText?.text.toString().toIntOrNull()
                if(p1!=null) povrsina_od = p1
                else povrsina_od = 0
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        view.povrsina_do.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var p1 = view.povrsina_do_layout.editText?.text.toString().toIntOrNull()
                if(p1!=null) povrsina_do = p1
                else povrsina_do = 0
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        view.cena_od.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var p1 = view.cena_od_layout.editText?.text.toString().toIntOrNull()
                if(p1!=null) cena_od = p1
                else cena_od = 0
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        view.cena_do.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var p1 = view.cena_do_layout.editText?.text.toString().toIntOrNull()
                if(p1!=null) cena_do = p1
                else cena_do = 0
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        view.broj_soba_filter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var p1 = view.broj_soba_layout.editText?.text.toString().toIntOrNull()
                if(p1!=null) broj_soba = p1
                else broj_soba = 0
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })


        

        return view
    }


}

