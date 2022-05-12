using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;
using PlatinumBCKND.OglasiData;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ObavestenjeController : ControllerBase
    {
        private IOglasiData _oglasiData;
        private IObavestenjeData _obavestenjaData;
        private IVlasnikData _vlasnikData;

        public ObavestenjeController(IObavestenjeData obavestenjaData, IOglasiData oglasiData, IVlasnikData vlasnikData)
        {
             _obavestenjaData = obavestenjaData;
            _oglasiData = oglasiData;
            _vlasnikData = vlasnikData;

        }


        [HttpGet]
        [Route("/api/svaObavestenjaKorisnika/{username}")]
        public IActionResult GetSva(String username)
        {
            return Ok(_obavestenjaData.GetSva(username));
        }

        [HttpGet]
        [Route("/api/imaLiNotifikacija/{idV}")]
        public IActionResult imaLiNotifikacija(Guid idV)
        {
            return Ok(_obavestenjaData.GetNew(idV));
        }


        [HttpGet]
        [Route("/api/obavestenje/{id}")]
        public IActionResult GetObavestenje(Guid id)
        {
            var obavestenje = _obavestenjaData.GetObavestenje(id);

            if (obavestenje != null)
            {
                return Ok(obavestenje);
            }
            else
            {
                return NotFound($"Obavestenje nije pronadjeno!");
            }
        }

        [HttpPost]
        [Route("/api/dodajObavestenje")]
        public IActionResult AddObavestenje(Obavestenje o)
        {
            _obavestenjaData.AddObavestenje(o);
            Console.WriteLine("OBAVESTENJE USPESNO DODATO");
            return Ok(o.idObavestenja);
        }


        [HttpGet]
        [Route("/api/brisiObavestenje/{id}")]

        public IActionResult DeleteObavestenje(Guid id)
        {
            var obavestenje = _obavestenjaData.GetObavestenje(id);
            if (obavestenje != null)
            {
                _obavestenjaData.DeleteObavestenje(obavestenje);
                return Ok();
            }
            else
            {
                return NotFound("Obavestenje ne postoji!");

            }
        }

        [HttpGet]
        [Route("/api/izmeniObavestenje/{idObavestenja}/{tru}")]

        public Obavestenje EditObavestenje(Guid idObavestenja, Boolean tru)
        {
            var o = _obavestenjaData.GetObavestenje(idObavestenja);
            if (o != null)
            {
                if (tru == true)
                {
                    o.prihvacen = 1;
                    _obavestenjaData.EditObavestenje(o);
                }
                else
                {
                    _obavestenjaData.DeleteObavestenje(o);
                }
            }
            return o;
        }




    }
}
