package com.codingwithmitch.kotlinrecyclerviewexample


import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.preference.PreferenceManager
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.codingwithmitch.kotlinrecyclerviewexample.AdRecyclerAdapter.Companion.flagSlika
import com.codingwithmitch.kotlinrecyclerviewexample.AdRecyclerAdapter.Companion.slikeOglasa
import com.example.skucise.R
import com.example.skucise.activity.ForgotPassword
import com.example.skucise.activity.OglasPage
import com.example.skucise.fragments.UserEditProfileFragment
import com.example.skucise.fragments.UserProfile
import com.example.skucise.model.Oglas
import com.example.skucise.model.OglasStan
import com.example.skucise.model.OmiljeniOglas
import com.example.skucise.model.SlikaOglasa
import com.example.skucise.util.Constants
import kotlinx.android.synthetic.main.activity_edit_profile.view.*
import kotlinx.android.synthetic.main.layout_ad_list_item.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.ArrayList


class AdRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    companion object
    {
        var flagSlika : Int = 200
        lateinit var idVlasnika: String //IZ SHARED PREFFS
        lateinit var viewPom : Context
        lateinit var decodedByte : Bitmap
        var oglasiLike = mutableListOf<OmiljeniOglas>()
        var items: MutableList<Oglas> = mutableListOf()
        var slikeOglasa :MutableList<SlikaOglasa> = mutableListOf()
    }

    private val TAG: String = "AppDebug"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        viewPom = parent.context
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(viewPom)
        val usernameLogovanog = sharedPref.getString("Username", " ")
        val obj = vratiJedan("vlasnikUser/" + usernameLogovanog)
        idVlasnika = obj.get("idVlasnika").toString()

        var res = StringBuffer()
        res = sendGetRequest("omiljeniOglasi/" + usernameLogovanog)

        sendSlikeRequest("dajSlikuOglasa")


        return AdViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_ad_list_item, parent, false)

        )


    }




    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder) {

            is AdViewHolder -> {
                holder.bind(items.get(position))
            }

        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList1(adList: List<OglasStan>){
        items.clear()
        for(o in adList)
        {
            items.add(o.oglas)
        }
    }
    fun submitList(adList: List<Oglas>){
        items=adList.toMutableList()
    }

    class AdViewHolder
    constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){
        val oglas = itemView.oglas_id
        val slika_oglasa = itemView.slika_oglasa
        val br_pregleda = itemView.ad_broj_pregleda
        val br_lajkova = itemView.ad_broj_lajkova
        val naslov_glasa = itemView.naslov_oglasa
        val opis_oglasa = itemView.opis_oglasa
        val cena_oglas = itemView.ad_price
        val prodaja_oglas = itemView.ad_sale_yesNo
        val favorite_ad = itemView.ad_favorite


        fun bind(adPost: Oglas){



            oglas.setOnClickListener{
                val intent = Intent(it.getContext(), OglasPage::class.java)
                intent.putExtra("id" , adPost.idOglasa.toString().trim())
                it.getContext().startActivity(intent)

            }



            //book = vratiJedan("dajSlikuOglasa/" + adPost.idOglasa)
            /*if(flagSlika == 200){
                if(book != null)
                {
                    val decodedString: ByteArray = Base64.decode(book.get("slika").toString(),
                        Base64.DEFAULT
                    )
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    /*var croppedBitmap: Bitmap
                    if(decodedByte.height > 900 || decodedByte.width > 400)
                    {
                        croppedBitmap = Bitmap.createBitmap(
                        decodedByte,
                        10,
                        10,
                        400,
                            900
                        )
                    }
                    else
                    {
                        croppedBitmap  = decodedByte
                    }*/

                    slika_oglasa.setImageBitmap(decodedByte)
                    //slika_oglasa.setImageBitmap(Bitmap.createScaledBitmap(croppedBitmap,  croppedBitmap.width , 800 , false))
                    slika_oglasa.layoutParams.height = 1000
                }}
            else
            {
                slika_oglasa.setBackgroundResource(R.drawable.ic_baseline_image_not_supported_24)
                slika_oglasa.layoutParams.height = 700

            }*/

            if(slikeOglasa.isEmpty())
            {
                var book :JSONObject
                book = vratiJedan("stan/" + adPost.idStana)
                var tipStanaa = book.get("idTipaStana").toString()
                when(tipStanaa.toInt())
                {
                    1 -> slika_oglasa.setBackgroundResource(R.drawable.ic_baseline_business_24)
                    2 -> slika_oglasa.setBackgroundResource(R.drawable.ic_baseline_apartment_24)
                    3 -> slika_oglasa.setBackgroundResource(R.drawable.ic_baseline_house_24)
                    4 -> slika_oglasa.setBackgroundResource(R.drawable.ic_baseline_business_center_24)
                    5 -> slika_oglasa.setBackgroundResource(R.drawable.ic_baseline_cabin_24)
                }
                slika_oglasa.setImageBitmap(null)
                slika_oglasa.layoutParams.height = 500
                slika_oglasa.layoutParams.width = 500
            }
            else{

                val s: SlikaOglasa? = slikeOglasa.find { it.idOglasa == adPost.idOglasa }
                if(s != null){
                println("SLIKA")

                    println("PROSO")
                    val decodedString: ByteArray = Base64.decode(s.slika,
                        Base64.DEFAULT
                    )
                    decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    /*var croppedBitmap: Bitmap
                    if(decodedByte.height > 900 || decodedByte.width > 400)
                    {
                        croppedBitmap = Bitmap.createBitmap(
                        decodedByte,
                        10,
                        10,
                        400,
                            900
                        )
                    }
                    else
                    {
                        croppedBitmap  = decodedByte
                    }*/

                    slika_oglasa.setImageBitmap(decodedByte)
                    slika_oglasa.setBackgroundResource(R.drawable.ic_baseline_check_box_outline_blank_24)
                    //slika_oglasa.setImageBitmap(Bitmap.createScaledBitmap(croppedBitmap,  croppedBitmap.width , 800 , false))
                    //slika_oglasa.layoutParams.height = 1000
                }
                else
                {
                    var book :JSONObject
                    book = vratiJedan("stan/" + adPost.idStana)
                    var tipStanaa = book.get("idTipaStana").toString()
                    when(tipStanaa.toInt())
                    {
                        1 -> slika_oglasa.setBackgroundResource(R.drawable.ic_baseline_business_24)
                        2 -> slika_oglasa.setBackgroundResource(R.drawable.ic_baseline_apartment_24)
                        3 -> slika_oglasa.setBackgroundResource(R.drawable.ic_baseline_house_24)
                        4 -> slika_oglasa.setBackgroundResource(R.drawable.ic_baseline_business_center_24)
                        5 -> slika_oglasa.setBackgroundResource(R.drawable.ic_baseline_cabin_24)
                    }
                    slika_oglasa.setImageBitmap(null)
                    slika_oglasa.layoutParams.height = 500
                    slika_oglasa.layoutParams.width = 500
                }
            }




            //IKONICA TIP STANA
            /*
            var book :JSONObject
            book = vratiJedan("stan/" + adPost.idStana)
            var tipStanaa = book.get("idTipaStana").toString()
            when(tipStanaa.toInt())
            {
                1 -> slika_oglasa.setBackgroundResource(R.drawable.ic_baseline_business_24)
                2 -> slika_oglasa.setBackgroundResource(R.drawable.ic_baseline_apartment_24)
                3 -> slika_oglasa.setBackgroundResource(R.drawable.ic_baseline_house_24)
                4 -> slika_oglasa.setBackgroundResource(R.drawable.ic_baseline_business_center_24)
                5 -> slika_oglasa.setBackgroundResource(R.drawable.ic_baseline_cabin_24)
            }*/

            /*Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(adPost.naslovOglasa)
                .into(slika_oglasa)*/

            br_pregleda.setText(adPost.brojPregleda.toString())
            br_lajkova.setText(adPost.brojLajkova.toString())
            naslov_glasa.setText(adPost.naslovOglasa)
            opis_oglasa.setText(adPost.opisOglasa)
            //slika_oglasa.setImageURI(R.drawable.house1)
            cena_oglas.setText((adPost.cena).toString()+"€")
            if(adPost.prodajaDaNe == 1)
                prodaja_oglas.setText("Na prodaju")
            else {
                prodaja_oglas.setText("Izdaje se")
                //prodaja_oglas.setBackgroundColor(Color.parseColor("#ffffff"))
            }

            for (o in oglasiLike)
            {
                if(adPost.idOglasa.equals(o.idOglasa) && adPost.idVlasnika.equals(o.idVlasnika))  {
                    favorite_ad.setImageResource(R.drawable.ic_baseline_favorite_36)
                    favorite_ad.tag = 1
                }
                else
                {
                    favorite_ad.setImageResource(R.drawable.ic_baseline_favorite_border_36)
                    favorite_ad.tag = 0
                }
            }

            favorite_ad.setOnClickListener {

                if(favorite_ad.tag == 1) {
                    favorite_ad.setImageResource(R.drawable.ic_baseline_favorite_border_36)
                    favorite_ad.tag = 0
                    //POZIV BACK BRISI OMILJENI
                    ReqOmiljeni("brisiOmiljeni/${adPost.idOglasa}/${idVlasnika}")
                    if(adPost.brojLajkova > 0)
                        adPost.brojLajkova = adPost.brojLajkova-1
                    br_lajkova.setText(( adPost.brojLajkova).toString())

                }
                else {
                    favorite_ad.setImageResource(R.drawable.ic_baseline_favorite_36)
                    favorite_ad.tag = 1
                    //POZIV BACK DODAJ OMILJENI
                    ReqOmiljeni("dodajOmiljeni/${adPost.idOglasa}/${idVlasnika}")
                    adPost.brojLajkova = adPost.brojLajkova+1
                    br_lajkova.setText((adPost.brojLajkova).toString())


                }
                favorite_ad.isEnabled = false

                favorite_ad.postDelayed(Runnable {
                    favorite_ad.isEnabled = true
                    Log.d(TAG, "resend1")
                }, 1000)
            }

        }




    }
}
private fun sendSlikeRequest(s: String) {
    val mURL = URL(Constants.BASE_URL + "api/" + s)
    val response = StringBuffer()
    var obj = JSONArray()
    with(mURL.openConnection() as HttpURLConnection) {

        requestMethod = "GET"

        println("URL : $url")
        println("Response Code : $responseCode")

        if(responseCode == 200) {
            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()
                while (inputLine != null) {
                    obj = JSONArray(inputLine)
                    for (i in 0 until obj.length()) {
                        val book = obj.getJSONObject(i)

                        var slika = SlikaOglasa(
                            book.get("idSlike").toString(),
                            book.get("idOglasa").toString(),
                            book.get("slika").toString()
                        )
                       slikeOglasa.add(slika)
                    }
                    inputLine = it.readLine()
                }
                it.close()

                println("RESPONSE > $response")
            }
        }
    }
}

//GET REQ
@RequiresApi(Build.VERSION_CODES.O)
public fun vratiJedan(requestText: String): JSONObject {

    val mURL = URL(Constants.BASE_URL + "api/" + requestText)
    val response = StringBuffer()
    var obj = JSONObject()
    with(mURL.openConnection() as HttpURLConnection) {

        requestMethod = "GET"

        println("URL : $url")
        println("Response Code : $responseCode")
        flagSlika = responseCode
        BufferedReader(InputStreamReader(inputStream)).use {
            var inputLine = it.readLine()
            while (inputLine != null) {
                obj = JSONObject(inputLine)
                inputLine = it.readLine()
            }
            it.close()

            println("RESPONSE > $response")
        }
    }
    return obj
}

//GET REQ
@RequiresApi(Build.VERSION_CODES.O)
public fun ReqOmiljeni(requestText: String) {

    val mURL = URL(Constants.BASE_URL + "api/" + requestText)
    val response = StringBuffer()
    var obj = JSONObject()
    with(mURL.openConnection() as HttpURLConnection) {

        requestMethod = "GET"

        println("URL : $url")
        println("Response Code : $responseCode")

        if(responseCode == 200) {
            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()
                while (inputLine != null) {

                    inputLine = it.readLine()
                }
                it.close()

                println("RESPONSE > $response")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun  sendGetRequest(requestText :String) :StringBuffer{

    val mURL = URL(Constants.BASE_URL + "api/"+requestText)
    val response = StringBuffer()
    with(mURL.openConnection() as HttpURLConnection) {

        requestMethod = "GET"

        var obj = JSONArray()
        println("URL : $url")
        println("Response Code : $responseCode")
        BufferedReader(InputStreamReader(inputStream)).use {
            var inputLine = it.readLine()

            AdRecyclerAdapter.oglasiLike.clear()
            while (inputLine != null) {
                response.append(inputLine)
                obj = JSONArray(inputLine)
                for (i in 0 until obj.length()) {
                    val book = obj.getJSONObject(i)

                    var oglas = OmiljeniOglas(
                        book.get("idVlasnika").toString(),
                        book.get("idOglasa").toString()
                    )
                    AdRecyclerAdapter.oglasiLike.add(oglas)
                    println(oglas)
                }
                inputLine = it.readLine()
            }

            it.close()
            println("RESPONSE AdRecycle> $response")
        }
    }
    return response
}

