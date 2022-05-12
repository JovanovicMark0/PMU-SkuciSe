using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class SlikaKorisnikaController : ControllerBase
    {
        private ISlikaKorisnikaData _iSlikaData;
        public SlikaKorisnikaController(ISlikaKorisnikaData iSlikaData)
        {
            _iSlikaData = iSlikaData;
        }

        [HttpGet]
        [Route("/api/dajSlikuKorisnika/{idKorisnika}")]
        public IActionResult GetSlikaKorisnika(Guid idKorisnika)
        {
            var slika = _iSlikaData.GetSlikaKorisnika(idKorisnika);
            if(slika != null)
            {
                return Ok(slika);
            }
            else
            {
                return NotFound($"Slika vlasnika sa indeksom {idKorisnika} nije pronadjena!");
            }
        }

        [HttpPost]
        [Route("/api/dodajSlikuKorisnika")]
        public IActionResult AddSlikaKorisnika(SlikaKorisnika slika)
        {
            _iSlikaData.AddSlikaKorisnika(slika);
            Console.WriteLine("Slika korisnika USPESNO DODATA");
            return Ok("OK");
        }


        
        [HttpPost]
        [Route("/api/izmeniSlikuKorisnika")]

        public IActionResult EditSlikeKorisnika(SlikaKorisnika slika)
        {
            var pomSlika = _iSlikaData.GetSlikaKorisnika(slika.idKorisnika);

            if (pomSlika != null)
            {
                _iSlikaData.EditSlikaKorisnika(slika);
            }
            else
            {
                _iSlikaData.AddSlikaKorisnika(slika);
            }
            return Ok("OK");
        }
    }
}
