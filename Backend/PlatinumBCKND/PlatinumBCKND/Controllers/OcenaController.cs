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
    public class OcenaController : ControllerBase
    {
        private IOcenaData _ocenaData;
        private OglasContext _cntx;

        public OcenaController(IOcenaData ocenaData, OglasContext cntx)
        {
            _ocenaData = ocenaData;
            _cntx = cntx;
        }



        [HttpGet]
        [Route("/api/oceneKorisnika/{id}")]
        public List<Ocena> GetOceneFrom(Guid id)
        {
            Console.WriteLine("Trazim ocene za korisnika > " + id);
            var result = _cntx.Ocena.ToList();
            result.Reverse();
            List<Ocena> ocene = new List<Ocena>();
            foreach (Ocena i in result)
            {
                if (i.idOcenjenog.Equals(id))
                    ocene.Add(i);
            }
            return ocene;
        }

        [HttpGet]
        [Route("/api/oceneKorisnikaUsername/{username}")]
        public List<Ocena> GetOceneFromK(String username)
        {
            Console.WriteLine("Trazim ocene za korisnika > " + username);
            var result = _cntx.Ocena.ToList();
            result.Reverse();
            List<Ocena> ocene = new List<Ocena>();
            foreach (Ocena i in result)
            {
                Vlasnik v = _cntx.Vlasnik.Where(p => p.username == username).FirstOrDefault();
                if (i.idOcenjenog.Equals(v.idVlasnika))
                    ocene.Add(i);
            }
            return ocene;
        }


        [HttpPost]
        [Route("/api/dodajOcenu")]
        public IActionResult AddOcena(Ocena ocena)
        {
            _ocenaData.AddOcena(ocena);
            Console.WriteLine("OCENA USPESNO DODATA");
            //return Created(HttpContext.Request.Scheme+ "://" + HttpContext.Request.Host + HttpContext.Request.Path + "/" + oglas.idOglasa, oglas);
            return Ok("OK");
        }

    }
}
