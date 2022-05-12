package com.example.skucise.model

import java.time.LocalDateTime

data class Oglas(
    val idOglasa: String,
    val idVlasnika: String,
    val idStana: String,
    val naslovOglasa: String,
    val opisOglasa: String,
    val cena: Int,
    val datumIsteka: LocalDateTime?,
    val prodajaDaNe: Int,
    var brojLajkova: Int,
    val brojPregleda : Int
)

data class FullOglas (
    val oglas: Oglas,
    val stan: Stan,
    val vlasnik: Vlasnik,
    val ostalo : Ostalo
)

data class OglasStan (
    val oglas: Oglas,
    val stan: Stan
)

data class Ostalo (
    val nazivGrada: String,
    val tipGradnje : String,
    val namestenost : String,
    val stanje: String,
    val tipStana : String
)
data class Stan(
    val idStana: String,
    val idVlasnika: String,
    val povrsina: Int,
    val grad: String,
    val adresa: String,
    val brojSoba: Int,
    val idTipaStana: Int,
    val idStanjaObjekta: Int,
    val idNamestenosti: Int,
    val idGradnje: Int,
    val sprat: Int,
    val terasa: Int,
    val telefon: Int,
    val internet: Int,
    val katv: Int,
    val interfon: Int,
    val lift: Int,
    val klima: Int,
    val grejanje: Int,
    val parking: Int,
    val videoNadzor: Int
)

data class Vlasnik(
    val idVlasnika: String, val username: String, val punoIme: String, val brojTelefona: String,
    val brojOglasa: Int, val zbirSvihOcena: Int, val brojOcena: Int, val email:String, val password :String
)

data class OmiljeniOglas (
    val idVlasnika: String, val idOglasa: String
)

data class Grad(val idGrada: Int, val nazivGrada: String)

data class Gradnja(val idGradnje: Int, val tipGradnje: String)

data class Namestenost(val idNamestenosti: Int, val namestenost: String)

data class StanjeObjekta(val idStanja: Int, val stanje: String)

data class TipStana(val idTipa: Int, val tipStana: String)

data class Ocena(val idOcene: String, val idOcenjenog: String, val idOcenjivaca: String, val ocena : Int, val komentar: String, val naslov: String)

data class SlikaKorisnika(val idKorisnika: String, val slika: String)

data class SlikaOglasa(val idSlike: String, val idOglasa : String ,  val slika: String)

data class Messages(val ID: String, val messageContent : String, val date: String,  val messageFrom: String,  val messageTo: String)
data class Obavestenje (
    val idObavestenja: String, val idOglasa: String, val idPosiljaoca: String, val idPrimaoca:String, val datum : String, val prihvacen: Int, val usernamePosiljaoca : String,var naslovOglasa:String
)