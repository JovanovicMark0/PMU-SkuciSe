using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using PlatinumBCKND.Interfaces;
using PlatinumBCKND.OglasiData;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class OmiljeniOglasiController : ControllerBase
    {
        private IOmiljeniOglasiData _oglasiData;
        

        public OmiljeniOglasiController(IOmiljeniOglasiData omi)
        {
            _oglasiData = omi;

        }


        [HttpGet]
        [Route("/api/omiljeniOglasi/{username}")]
        public IActionResult GetOmiljeniOglasi(String username)
        {
            var result = _oglasiData.GetOmiljeniOglasi(username);
            result.Reverse();
            return Ok(result);
        }

        [HttpGet]
        [Route("/api/dodajOmiljeni/{idOglasa}/{idVlasnika}")]
        public IActionResult AddOmiljeniOglas(Guid idOglasa, Guid idVlasnika)
        {

            _oglasiData.AddOmiljeniOglas(idOglasa, idVlasnika);

            return Created(HttpContext.Request.Scheme + "://" + HttpContext.Request.Host + HttpContext.Request.Path + "/" + idOglasa, idVlasnika);
        }

        [HttpGet]
        [Route("/api/brisiOmiljeni/{idOglasa}/{idVlasnika}")]
        public IActionResult BrisiOmiljeni(Guid idOglasa, Guid idVlasnika)
        {
            _oglasiData.DeleteOmiljeniOglas(idOglasa, idVlasnika);

            return Created(HttpContext.Request.Scheme + "://" + HttpContext.Request.Host + HttpContext.Request.Path + "/" + idOglasa, idVlasnika);
        }
    }
}