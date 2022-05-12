using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;
using PlatinumBCKND.OglasiData;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Controlers
{
    [Route("api/[controller]")]
    [ApiController]
    public class OglasiController : ControllerBase
    {
        private IOglasiData _oglasiData;
        private IOmiljeniOglasiData _omiljeniData;
        private ISlikaOglasaData _slikaData;
        private IStanData _stanData;
        private IGradData _gradData;
        private IGradnjaData _gradnjaData;
        private INamestenostData _namestenostData;
        private IStanjeObjektaData _stanjeObjektaData;
        private ITipStanaData _tipStanaData;
        private IVlasnikData _vlasnikData;

        public OglasiController(IOmiljeniOglasiData omiljeniData, ISlikaOglasaData slikaData, IOglasiData oglasiData, IStanData stanData, IGradData gradData, IGradnjaData gradnjaData, INamestenostData namestenostData, IStanjeObjektaData stanjeObjektaData, ITipStanaData tipStanaData , IVlasnikData vlasnikData)
        {
            _omiljeniData = omiljeniData;
            _slikaData = slikaData;
            _oglasiData = oglasiData;
            _stanData = stanData;
            _gradData = gradData;
            _gradnjaData = gradnjaData;
            _namestenostData = namestenostData;
            _stanjeObjektaData = stanjeObjektaData;
            _tipStanaData = tipStanaData;
            _vlasnikData = vlasnikData;

    }

        [HttpGet]
        [Route("/api/sviOglasi")]
        public IActionResult GetOglas()
        {
            return Ok(_oglasiData.GetOglas());
        }

        [HttpGet]
        [Route("/api/sviOglasiKorisnika/{id}")]
        public IActionResult GetOglaseKorisnika(Guid id)
        {
            return Ok(_oglasiData.GetOglaseKorisnika(id));
        }

        [HttpGet]
        [Route("/api/oglas/{id}")]
        public IActionResult GetOglas(Guid id)
        {
            var oglas = _oglasiData.GetOglas(id);
           
            if(oglas != null)
            {  
                var stan = _stanData.GetStan(oglas.idStana);

                if (stan != null)
                {
                    var gradnja = _gradnjaData.GetGradnja(stan.idGradnje);
                    var namestenost = _namestenostData.GetNamestenost(stan.idNamestenosti);
                    var stanjeObjekta = _stanjeObjektaData.GetStanjeObjekta(stan.idStanjaObjekta);
                    var tipStana = _tipStanaData.GetTipStana(stan.idTipaStana);
                    var vlasnik = _vlasnikData.GetVlasnik(stan.idVlasnika);

                    OglasFull fullOglas = new OglasFull();

                    fullOglas.idOglasa = oglas.idOglasa;
                    fullOglas.idStana = oglas.idStana;
                    fullOglas.idVlasnika = oglas.idVlasnika;
                    fullOglas.stan = stan;
                    fullOglas.namestenost = namestenost.namestenost;
                    fullOglas.naslovOglasa = oglas.naslovOglasa;
                    fullOglas.nazivGrada = stan.grad;
                    fullOglas.opisOglasa = oglas.opisOglasa;
                    fullOglas.prodajaDaNe = oglas.prodajaDaNe;
                    fullOglas.stanje = stanjeObjekta.stanje;
                    fullOglas.tipGradnje = gradnja.tipGradnje;
                    fullOglas.tipStana = tipStana.tipStana;
                    fullOglas.cena = oglas.cena;
                    fullOglas.brojPregleda = oglas.brojPregleda;
                    fullOglas.brojLajkova = oglas.brojLajkova;

                    
                    return Ok(fullOglas);
                }
                Console.WriteLine($"Stan sa indeksom {oglas.idStana} nije pronadjen!");
                return NotFound($"Stan sa indeksom {oglas.idStana} nije pronadjen!");
            }
            else
            {
                Console.WriteLine($"Oglas sa indeksom {id} nije pronadjen!");
                return NotFound($"Oglas sa indeksom {id} nije pronadjen!");
            }
        }

        [HttpPost]
        [Route("/api/dodajOglas")]
        public IActionResult AddOglas(Oglas oglas)
        {
            _oglasiData.AddOglas(oglas);
            Console.WriteLine("OGLAS USPESNO DODAT");
            //return Created(HttpContext.Request.Scheme+ "://" + HttpContext.Request.Host + HttpContext.Request.Path + "/" + oglas.idOglasa, oglas);
            return Ok(oglas.idOglasa);
        }


        [HttpGet]
        [Route("/api/brisiOglas/{id}")]

        public IActionResult DeleteOglas(Guid id)
        {
            var oglas = _oglasiData.GetOglas(id);
            if(oglas != null)
            {
                _omiljeniData.DeleteOmiljeniOglasDelOglas(id);
                _stanData.DeleteStanDelOglas(oglas.idStana);
                _slikaData.DeleteSlikeOglasa(id);

                _oglasiData.DeleteOglas(oglas);
                return Ok();
            }
            else
            {
                return NotFound($"Oglas sa indeksom {id} nije pronadjen!");

            }
        }

        [HttpPost]
        [Route("/api/izmeniOglas")]

        public String EditOglas(Oglas oglas)
        {
            var pomOglas = _oglasiData.GetOglas(oglas.idOglasa);

            if(pomOglas != null)
            {
                oglas.idOglasa = pomOglas.idOglasa;
                _oglasiData.EditOglas(oglas);
            }
            return "OK";
        }



        
    }
}
