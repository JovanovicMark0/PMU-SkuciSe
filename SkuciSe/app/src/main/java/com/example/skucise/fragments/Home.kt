package com.example.skucise.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingwithmitch.kotlinrecyclerviewexample.AdRecyclerAdapter
import com.example.skucise.R
import com.example.skucise.fragments.Home.Companion.adAdapter
import com.example.skucise.fragments.Home.Companion.data
import com.example.skucise.fragments.Home.Companion.dataCopy
import com.example.skucise.fragments.Home.Companion.oglasi
import com.example.skucise.fragments.Home.Companion.oglasiSearch
import com.example.skucise.fragments.Home.Companion.oglasiFilter
import com.example.skucise.model.Oglas
import com.example.skucise.model.OglasStan
import com.example.skucise.model.Stan
import com.example.skucise.util.Constants
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.aactivity_main.*
import kotlinx.android.synthetic.main.aactivity_main.view.*
import kotlinx.android.synthetic.main.activity_oglas_page.*
import kotlinx.android.synthetic.main.fragment_filteri.*
import kotlinx.android.synthetic.main.fragment_filteri.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime

class Home : Fragment() {

    companion object{
        lateinit var adAdapter: AdRecyclerAdapter
        lateinit var viewPom : View
        lateinit var drawer : DrawerLayout
        lateinit var buttonFilteri: Button
        lateinit var buttonPtimeniFiltere : Button
        lateinit var buttonPonistiFiltere : Button
        var oglasi = mutableListOf<Oglas>()
        var data = mutableListOf<OglasStan>()
        var dataCopy = mutableListOf<OglasStan>()
        var oglasiSearch = mutableListOf<OglasStan>()
        var oglasiFilter = mutableListOf<OglasStan>()
        var stanovi = mutableListOf<OglasStan>()
        var tmp = mutableListOf<OglasStan>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater!!.inflate(R.layout.aactivity_main, container, false)

        viewPom = view
        searching(view.home_search_id)
        sendGetRequest("sviOglasi")


        var book = JSONObject()
        stanovi.clear()
        data.clear()
        dataCopy.clear()
        for(o in oglasi)
        {
            //STAN
            book =  sendGetRequest1("stan/${o.idStana}")

            var stan =  Stan(
                book.get("idStana").toString(),
                book.get("idVlasnika").toString(),
                (book.get("povrsina").toString()).toInt(),
                book.get("grad").toString(),
                book.get("adresa").toString(),
                (book.get("brojSoba").toString()).toInt(),
                (book.get("idTipaStana").toString()).toInt(),
                (book.get("idStanjaObjekta").toString()).toInt(),
                (book.get("idNamestenosti").toString()).toInt(),
                (book.get("idGradnje").toString()).toInt(),
                (book.get("sprat").toString()).toInt(),
                (book.get("terasa").toString()).toInt(),
                (book.get("telefon").toString()).toInt(),
                (book.get("internet").toString()).toInt(),
                (book.get("katv").toString()).toInt(),
                (book.get("interfon").toString()).toInt(),
                (book.get("lift").toString()).toInt(),
                (book.get("klima").toString()).toInt(),
                (book.get("grejanje").toString()).toInt(),
                (book.get("parking").toString()).toInt(),
                (book.get("videoNadzor").toString()).toInt()
            )

            stanovi.add(OglasStan(o,stan))
            data.add(OglasStan(o,stan))
            dataCopy.add(OglasStan(o,stan))
        }

        view.aamain_spinnerSort.setSelection(6)

        view.aamain_spinnerSort.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                initRecyclerView(Home.viewPom)
                val selectedItem = parent.getItemAtPosition(position).toString()
                if (selectedItem == "Po ceni rastuće") {
                    println("Sort po ceni <!")
                    addDataSet(2)
                }
                else if (selectedItem == "Po ceni opadajuće") {
                    println("Sort po ceni > !")
                    addDataSet(6)
                }
                else if (selectedItem == "Po broju pregleda rastuće") {
                    println("Sort po broju pregleda!")
                    addDataSet(3)
                }
                else if (selectedItem == "Po broju pregleda opadajuće") {
                    println("Sort po broju pregleda!")

                    addDataSet(7)
                }
                else if (selectedItem == "Po broju praćenja rastuće") {
                    println("Sort po broju pracenja!")

                    addDataSet(4)
                }
                else if (selectedItem == "Po broju praćenja opadajuće") {

                    addDataSet(8)

                }
                else {
                    println("Sort po defaultu!")

                    addDataSet(5)
                }
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        drawer = view.findViewById(R.id.drawer_layout)
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        buttonFilteri = view.findViewById(R.id.btn_filteri)
        buttonFilteri.setOnClickListener {
            drawer.openDrawer(GravityCompat.END,true)

        }
        primeniFiltere()
        buttonPtimeniFiltere = view.findViewById(R.id.primeni_filtere)
        buttonPtimeniFiltere.setOnClickListener {
            primeniFiltere()
        }

        buttonPonistiFiltere = view.findViewById(R.id.btn_ponisti_filtere)
        buttonPonistiFiltere.setOnClickListener {

            Filteri.flagProdaja = 0
            view.button_prodaja.setBackgroundColor(Color.GRAY)
            Filteri.flagIzdavanje = 0
            view.button_izdavanje.setBackgroundColor(Color.GRAY)
            Filteri.grad = "Izaberi grad"
            view.filter_gradovi.setSelection(0)
            Filteri.struktura_stana = "Izaberi strukturu stana"
            view.filter_kategorija.setSelection(0)
            Filteri.namestenost = "Izaberi nameštenost"
            view.filter_namestenost.setSelection(0)
            Filteri.stanje = "Izaberi stanje objekta"
            view.filter_stanjeObjekta.setSelection(0)
            Filteri.gradnja = "Izaberi gradnju"
            view.filter_gradnja.setSelection(0)
            Filteri.povrsina_od = 0
            view.povrsina_od.setText("")
            Filteri.povrsina_do = 0
            view.povrsina_do.setText("")
            Filteri.cena_od = 0
            view.cena_od.setText("")
            Filteri.cena_do = 0
            view.cena_do.setText("")
            Filteri.broj_soba = 0
            view.broj_soba_filter.setText("")

            drawer.closeDrawer(GravityCompat.END,true)

            initRecyclerView(viewPom)
            addDataSet(15)
        }

        return view
    }

    fun primeniFiltere()
    {
        drawer.closeDrawer(GravityCompat.END,true)
        var prodaja = Filteri.flagProdaja
        var izdavanje = Filteri.flagIzdavanje

        if(prodaja%2!=0)
        {
            prodaja=1
        }
        else
        {
            prodaja = 0
        }
        if(izdavanje%2!=0)
        {
            izdavanje=1
        }
        else
        {
            izdavanje=0
        }
        data.clear()
        data = dataCopy.toMutableList()
        oglasiFilter.clear()
        for (o in data) {

            if (o.oglas.prodajaDaNe==prodaja)
            {
                if(gradovi(o) && strukturaStana(o) && namestenost(o) && stanje(o) && gradnja(o) && povrsina(o) && cena(o) && brojSoba(o))
                    oglasiFilter.add(o)
            }
            if(o.oglas.prodajaDaNe!=izdavanje && o.oglas.prodajaDaNe!=prodaja)
            {
                if(gradovi(o) && strukturaStana(o) && namestenost(o) && stanje(o) && gradnja(o) && povrsina(o) && cena(o) && brojSoba(o))
                    oglasiFilter.add(o)
            }



        }
        initRecyclerView(viewPom)
        addDataSet(9)
    }

    private fun gradovi(oglas : OglasStan) : Boolean
    {
        return if(Filteri.grad=="Izaberi grad") true
        else return oglas.stan.grad==Filteri.grad
    }

    private fun strukturaStana(oglas : OglasStan) : Boolean
    {
        return if(Filteri.struktura_stana=="Izaberi strukturu stana") true
        else {
            var id = oglas.stan.idTipaStana
            var struktura = ""
            if(id==1) struktura = "Garsonjera"
            if(id==2) struktura = "Stan"
            if(id==3) struktura = "Kuća"
            if(id==4) struktura = "Poslovni prostor"
            if(id==5) struktura = "Vikendica"

            return struktura==Filteri.struktura_stana
        }

    }

    private fun namestenost(oglas : OglasStan) : Boolean
    {
        return if(Filteri.namestenost=="Izaberi nameštenost") {
            true
        } else {
            var id = oglas.stan.idNamestenosti
            var n = ""
            if(id==1) n = "Nenamešten"
            if(id==2) n = "Polunamešten"
            if(id==3) n = "Namešten"

            return n==Filteri.namestenost
        }

    }

    private fun stanje(oglas : OglasStan) : Boolean
    {
        return if(Filteri.stanje=="Izaberi stanje objekta") true
        else {
            var id = oglas.stan.idStanjaObjekta
            var n = ""
            if(id==1) n = "Izvorno"
            if(id==2) n = "Renoviran"
            if(id==3) n = "Potrebno renoviranje"

            return n==Filteri.stanje
        }

    }

    private fun gradnja(oglas : OglasStan) : Boolean
    {
        return if(Filteri.gradnja=="Izaberi gradnju") true
        else {
            var id = oglas.stan.idGradnje
            var n = ""
            if(id==1) n = "Stara gradnja"
            if(id==2) n = "Nova gradnja"

            return n==Filteri.gradnja
        }

    }

    private fun povrsina(oglas: OglasStan) : Boolean
    {
        var p_od = Filteri.povrsina_od
        var p_do = Filteri.povrsina_do

        println("Povrsina od " + p_od + " " + p_do)

        if(p_od!=0 && p_do==0)
        {
            return oglas.stan.povrsina>=p_od
        }
        if(p_od==0 && p_do!=0)
        {
            return oglas.stan.povrsina<=p_do
        }
        if(p_od!=0 && p_do!=0)
        {
            return oglas.stan.povrsina in p_od..p_do
        }
        return true
    }

    private fun cena(oglas: OglasStan) : Boolean
    {
        var c_od = Filteri.cena_od
        var c_do = Filteri.cena_do

        if(c_od!=0 && c_do==0)
        {
            return oglas.oglas.cena>=c_od
        }
        if(c_od==0 && c_do!=0)
        {
            return oglas.oglas.cena<=c_do
        }
        if(c_od!=0 && c_do!=0)
        {
            return oglas.oglas.cena in c_od..c_do
        }
        return true
    }
    private fun brojSoba(oglas: OglasStan) : Boolean
    {
        var br_soba = Filteri.broj_soba

        return if (br_soba==0) true
        else return (br_soba!=0 && oglas.stan.brojSoba==br_soba)
    }


    private fun searching(search: androidx.appcompat.widget.SearchView) {
        search.home_search_id.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                oglasiSearch.clear()
                //oglasiSearch = oglasi.filter { o -> o.naslovOglasa == search.home_search_id.query.toString()} as MutableList<Oglas>
                for (o in data) {
                    if (o.oglas.naslovOglasa.lowercase().contains(search.home_search_id.query.toString().lowercase()))
                        oglasiSearch.add(o)
                }
                initRecyclerView(viewPom)
                addDataSet(1)

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(search.home_search_id.query.toString() == "") {
                    initRecyclerView(viewPom)
                    addDataSet(15)
                }
                return true
            }


        })
    }


    //radi primer poziva je sendGetRequest("sviOglasi")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun  sendGetRequest(requestText :String) :StringBuffer{

        val mURL = URL(Constants.BASE_URL + "api/"+requestText)
        val response = StringBuffer()
        with(mURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "GET"

            var obj = JSONArray()
            println("URL : $url")
            println("Response Code : $responseCode")
            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()

                oglasi.clear()
                while (inputLine != null) {
                    response.append(inputLine)
                    obj = JSONArray(inputLine)



                    for (i in 0 until obj.length()) {
                        val book = obj.getJSONObject(i)
                        println("${book.get("idOglasa")} by ${book.get("idVlasnika")}")
                        // 11/20/2021

                        var date = LocalDateTime.parse(book.get("datumIsteka").toString().trim())

                        var oglas = Oglas(book.get("idOglasa").toString(), book.get("idVlasnika").toString(), book.get("idStana").toString() ,book.get("naslovOglasa").toString(), book.get("opisOglasa").toString() , (book.get("cena").toString()).toInt() , date ,(book.get("prodajaDaNe").toString()).toInt() ,(book.get("brojLajkova").toString()).toInt() ,(book.get("brojPregleda").toString()).toInt())
                        oglasi.add(oglas)
                        println(oglas)
                    }
                    //var oglas =  Oglas(obj[1][0])


                    inputLine = it.readLine()
                }
                it.close()
                println("RESPONSE > $response")
            }
        }
        return response
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendGetRequest1(requestText: String): JSONObject {

        val mURL = URL(Constants.BASE_URL + "api/" + requestText)
        var obj = JSONObject()
        with(mURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "GET"

            println("URL : $url")
            println("Response Code : $responseCode")
            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()
                while(inputLine != null)
                {
                    obj = JSONObject(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                return obj
            }
        }
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView(viewPom)
        addDataSet(0)
    }

}

private fun addDataSet(br : Int):Boolean{ //AKO JE NULA ONDA SVI OGLASI AKO JE 1 ONDA SEARCH ... 2 filter 3 sort..
    when(br)
    {
        1 ->
        {
            data = oglasiSearch.toMutableList()
        }
        2 ->
        {
            data = data.sortedBy { o -> o.oglas.cena } as MutableList<OglasStan>
        }
        3 ->
        {
            data = data.sortedBy { o -> o.oglas.brojPregleda } as MutableList<OglasStan>
        }
        4 ->
        {
            data = data.sortedBy { o -> o.oglas.brojLajkova } as MutableList<OglasStan>
        }
        5 ->
        {
            data = data.toMutableList()
        }
        6 ->
        {
            data = data.sortedByDescending { o -> o.oglas.cena } as MutableList<OglasStan>
        }
        7 ->
        {
            data = data.sortedByDescending { o -> o.oglas.brojPregleda } as MutableList<OglasStan>
        }
        8 ->
        {
            data = data.sortedByDescending { o -> o.oglas.brojLajkova } as MutableList<OglasStan>
        }
        9 ->
        {
            data = oglasiFilter.toMutableList()
        }
        else ->
        {
            data = dataCopy.toMutableList()
        }
    }
    println("DATA785 > " + data)
    adAdapter.submitList1(data)
    return true
}

private fun initRecyclerView(view: View){

    view.recycler_view.apply {
        layoutManager = LinearLayoutManager(context)
        adAdapter = AdRecyclerAdapter()
        adapter = adAdapter
    }
}