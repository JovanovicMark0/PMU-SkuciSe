package com.example.skucise.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skucise.model.*

import kotlinx.coroutines.launch
import retrofit2.Response


class MainViewModel(private val repository: Repository) : ViewModel(){

    val myResponse : MutableLiveData<Response<Stan>> = MutableLiveData()
    val myResponse1 : MutableLiveData<Response<String>> = MutableLiveData()
    val myResponse2 : MutableLiveData<Response<Vlasnik>> = MutableLiveData()
    val myResponse3 : MutableLiveData<Response<String>> = MutableLiveData()
    val myResponse4 : MutableLiveData<Response<Boolean>> = MutableLiveData()

    fun pushPost(post: Stan)
    {
        viewModelScope.launch {
           val response = repository.pushPost(post)
            println(response)
            myResponse.value = response
        }
    }

    fun imaLiNotifikacija(idVlasnika: String)
    {
        viewModelScope.launch {
            val response = repository.imaLiNotifikacija(idVlasnika)
            println(response)
            myResponse4.value = response
        }
    }


    fun zaboravljenaLozinka(post: Vlasnik)
    {
        viewModelScope.launch {
            val response = repository.zaboravljenaLozinka(post)
            println(response)
            myResponse4.value = response
        }
    }

    fun pushOglas(post: Oglas)
    {
        viewModelScope.launch {
            val response = repository.pushOglas(post)
            println(response)
            myResponse1.value = response
        }
    }

    fun dodajObavestenje(post: Obavestenje)
    {
        viewModelScope.launch {
            val response = repository.dodajObavestenje(post)
            println(response)
            myResponse1.value = response
        }
    }

    fun izmeniOglas(post: Oglas)
    {
        viewModelScope.launch {
            val response = repository.izmeniOglas(post)
            println(response)
            myResponse1.value = response
        }
    }

    fun izmeniStan(post: Stan)
    {
        viewModelScope.launch {
            val response = repository.izmeniStan(post)
            println(response)
            myResponse1.value = response
        }
    }

    fun dodajKorisnika(post: Vlasnik)
    {
        viewModelScope.launch {
            val response = repository.dodajKorisnika(post)
            println(response)
            myResponse2.value = response
        }
    }

    fun dodajSlikuKorisnika(post: SlikaKorisnika)
    {
        viewModelScope.launch {
            val response = repository.dodajSlikuKorisnika(post)
            println(response)
            myResponse3.value = response
        }
    }

    fun izmeniSlikuKorisnika(post: SlikaKorisnika)
    {
        viewModelScope.launch {
            val response = repository.izmeniSlikuKorisnika(post)
            println(response)
            myResponse3.value = response
        }
    }

    fun izmeniVlasnika(post: Vlasnik)
    {
        viewModelScope.launch {
            val response = repository.izmeniVlasnika(post)
            println(response)
            myResponse2.value = response
        }
    }

    fun dodajSlikeOglasa(post: List<SlikaOglasa>)
    {
        viewModelScope.launch {
            val response = repository.dodajSlikeOglasa(post)
            println(response)
            myResponse3.value = response
        }
    }

    fun izmeniSlikeOglasa(post: List<SlikaOglasa>)
    {
        viewModelScope.launch {
            val response = repository.izmeniSlikeOglasa(post)
            println(response)
            myResponse3.value = response
        }
    }

    fun dodajOcenu(post: Ocena)
    {
        viewModelScope.launch {
            val response = repository.dodajOcenu(post)
            println(response)
            myResponse3.value = response
        }
    }
}