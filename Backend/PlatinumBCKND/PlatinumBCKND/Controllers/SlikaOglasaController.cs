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
    public class SlikaOglasaController : ControllerBase
    {
        private ISlikaOglasaData _iSlikaData;
        public SlikaOglasaController(ISlikaOglasaData iSlikaData)
        {
            _iSlikaData = iSlikaData;
        }

        [HttpGet]
        [Route("/api/dajSlikuOglasa")]
        public IActionResult GetSlikaOglasa()
        {
            var s = _iSlikaData.GetSlikaOglasa();
            return Ok(s);
            
        }

        [HttpGet]
        [Route("/api/dajSveSlikeOglasa/{idOglasa}")]
        public IActionResult GetSveSlikeOglasa(Guid idOglasa)
        {
            var s = _iSlikaData.GetSveSlikeOglasa(idOglasa);
            return Ok(s);
        }

        [HttpPost]
        [Route("/api/dodajSlikeOglasa")]
        public IActionResult AddSlikeOglasa(List<SlikaOglasa> slike)
        {
            _iSlikaData.AddSlikeOglasa(slike);
            Console.WriteLine("Slike oglasa USPESNO DODATE");
            return Ok("OK");
        }



        [HttpPost]
        [Route("/api/izmeniSlikeOglasa")]

        public IActionResult EditSlikeOglasa(List<SlikaOglasa> slike)
        {

            if (slike.Count > 0) {
                _iSlikaData.DeleteSlikeOglasa(slike.First().idOglasa);
            }
            _iSlikaData.AddSlikeOglasa(slike);
            return Ok("OK");
        }


    }
}


