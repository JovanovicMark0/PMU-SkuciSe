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
    public class VlasnikController : ControllerBase
    {
        private IVlasnikData _vlasnikData;
        
        public VlasnikController(IVlasnikData vlasnikData)
        {
            _vlasnikData = vlasnikData;
        }


        [HttpGet]
        [Route("/api/vlasnikUser/{username}")]
        public IActionResult GetVlasnik(String username)
        {
            var vlasnik = _vlasnikData.GetVlasnikU(username);
            
            if (vlasnik != null)
            {
                return Ok(vlasnik);
            }
            else
            {
                return NotFound($"vlasnik sa korisnickim imenom > {username} < nije pronadjen!");
            }
        }

        [HttpGet]
        [Route("/api/vlasnik/{id}")]
        public IActionResult GetVlasnik(Guid id)
        {
            var vlasnik = _vlasnikData.GetVlasnik(id);

            if (vlasnik != null)
            {
                return Ok(vlasnik);
            }
            else
            {
                return NotFound($"vlasnik sa indeksom {id} nije pronadjen!");
            }
        }

        [HttpPost]
        [Route("/api/vlasnik")]
        public IActionResult GetOglas(Vlasnik vlasnik)
        {

            _vlasnikData.AddVlasnik(vlasnik);
           
            return Created(HttpContext.Request.Scheme + "://" + HttpContext.Request.Host + HttpContext.Request.Path + "/" + vlasnik.idVlasnika, vlasnik);
        }


        [HttpDelete]
        [Route("/api/brisiVlasnika/{id}")]

        public IActionResult DeleteVlasnik(Guid id)
        {
            var vlasnik = _vlasnikData.GetVlasnik(id);
            if (vlasnik != null)
            {
                _vlasnikData.DeleteVlasnik(vlasnik);
                return Ok();
            }
            else
            {
                return NotFound($"vlasnik sa indeksom {id} nije pronadjen!");

            }
        }

        [HttpPost]
        [Route("/api/izmeniVlasnika")]

        public IActionResult EditVlasnika(Vlasnik vlasnik)
        {
            var pomV = _vlasnikData.GetVlasnik(vlasnik.idVlasnika);

            if (pomV != null)
            {
                _vlasnikData.EditVlasnik(vlasnik);
            }
            return Ok(pomV);
        }
    }
}
