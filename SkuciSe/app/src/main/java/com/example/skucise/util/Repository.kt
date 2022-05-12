package com.example.skucise.util

import com.example.skucise.model.*
import okhttp3.MultipartBody
import retrofit2.Response


class Repository {

    suspend fun pushPost(post: Stan): Response<Stan> {
        return RetrofitInstance.api.pushPost(post)
    }

    suspend fun zaboravljenaLozinka(post: Vlasnik): Response<Boolean> {
        return RetrofitInstance.api.zaboravljenaLozinka(post)
    }

    suspend fun imaLiNotifikacija(idVlasnika: String): Response<Boolean> {
        return RetrofitInstance.api.imaLiNotifikacija(idVlasnika)
    }

    suspend fun pushOglas(post: Oglas) : Response<String>{
        return RetrofitInstance.api.pushOglas(post)
    }// iza ) a pre {: Response<Oglas>

    suspend fun dodajObavestenje(post: Obavestenje) : Response<String> {
        return RetrofitInstance.api.dodajObavestenje(post)
    }

    suspend fun izmeniOglas(post: Oglas) : Response<String>{
        return RetrofitInstance.api.izmeniOglas(post)
    }

    suspend fun izmeniStan(post: Stan) : Response<String>{
        return RetrofitInstance.api.izmeniStan(post)
    }

    suspend fun izmeniVlasnika(post: Vlasnik) : Response<Vlasnik>{
        return RetrofitInstance.api.izmeniVlasnika(post)
    }

    suspend fun dodajKorisnika(post: Vlasnik): Response<Vlasnik> {
        return RetrofitInstance.api.dodajKorisnika(post)
    }

    suspend fun dodajSlikuKorisnika(post: SlikaKorisnika): Response<String> {
        return RetrofitInstance.api.dodajSlikuKorisnika(post)
    }

    suspend fun izmeniSlikuKorisnika(post: SlikaKorisnika): Response<String> {
        return RetrofitInstance.api.izmeniSlikuKorisnika(post)
    }

    suspend fun dodajSlikeOglasa(post: List<SlikaOglasa>): Response<String> {
        return RetrofitInstance.api.dodajSlikeOglasa(post)
    }

    suspend fun izmeniSlikeOglasa(post: List<SlikaOglasa>): Response<String> {
        return RetrofitInstance.api.izmeniSlikeOglasa(post)
    }

    suspend fun dodajOcenu(post: Ocena): Response<String> {
        return RetrofitInstance.api.dodajOcenu(post)
    }
}