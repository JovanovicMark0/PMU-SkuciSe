package com.example.skucise.util

import com.example.skucise.model.*
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface API {

    @POST("api/dodajStan")
    suspend fun pushPost(
        @Body post: Stan
    ): Response<Stan>


    @POST("api/dodajOglas")
    suspend fun pushOglas(
        @Body post: Oglas
    ): Response<String>

    @POST("api/register")
    suspend fun dodajKorisnika(
        @Body post: Vlasnik
    ): Response<Vlasnik>

    @POST("api/zaboravljenaLozinka")
    suspend fun zaboravljenaLozinka(
        @Body post: Vlasnik
    ): Response<Boolean>

    @POST("api/dodajSlikuKorisnika")
    suspend fun dodajSlikuKorisnika(
        @Body post: SlikaKorisnika
    ): Response<String>

    @POST("api/dodajObavestenje")
    suspend fun dodajObavestenje(
        @Body post: Obavestenje
    ): Response<String>


    @POST("api/izmeniSlikuKorisnika")
    suspend fun izmeniSlikuKorisnika(
        @Body post: SlikaKorisnika
    ): Response<String>

    @POST("api/dodajSlikeOglasa")
    suspend fun dodajSlikeOglasa(
        @Body post: List<SlikaOglasa>
    ): Response<String>

    @POST("api/izmeniSlikeOglasa")
    suspend fun izmeniSlikeOglasa(
        @Body post: List<SlikaOglasa>
    ): Response<String>

    @POST("api/dodajOcenu")
    suspend fun dodajOcenu(
        @Body post: Ocena
    ): Response<String>

    @GET("api/imaLiNotifikacija/{idVlasnika}")
    suspend fun imaLiNotifikacija(
      @Path("idVlasnika") idVlasnika: String
    ): Response<Boolean>

//EDIT OGLASA
    @POST("api/izmeniOglas")
    suspend fun izmeniOglas(
        @Body post: Oglas
    ): Response<String>


    @POST("api/izmeniStan")
    suspend fun izmeniStan(
        @Body post: Stan
    ): Response<String>

    @POST("api/izmeniVlasnika")
    suspend fun izmeniVlasnika(
        @Body post: Vlasnik
    ): Response<Vlasnik>

}
