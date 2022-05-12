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
    public class StanController : ControllerBase
    {
        private IStanData _stanData;
        public StanController(IStanData stanData)
        {
            _stanData = stanData;
        }

        [HttpGet]
        [Route("/api/sviStanovi")]
        public IActionResult GetStan()
        {
            return Ok(_stanData.GetStan());
        }

        [HttpGet]
        [Route("/api/stan/{id}")]
        public IActionResult GetStan(Guid id)
        {
            var oglas = _stanData.GetStan(id);

            if (oglas != null)
            {
                return Ok(oglas);
            }
            else
            {
                return NotFound($"Stan sa indeksom {id} nije pronadjen!");
            }
        }

        [HttpPost]
        [Route("/api/dodajStan")]
        public IActionResult AddStan(Stan stan)
        {
            _stanData.AddStan(stan);
            Console.WriteLine("STAN USPESNO DODAT");
            return Created(HttpContext.Request.Scheme + "://" + HttpContext.Request.Host + HttpContext.Request.Path + "/" + stan.idStana, stan);
            
        }

 

        [HttpDelete]
        [Route("/api/brisiStan/{id}")]

        public IActionResult DeleteOglas(Guid id)
        {
            var stan = _stanData.GetStan(id);
            if (stan != null)
            {
                _stanData.DeleteStan(stan);
                return Ok();
            }
            else
            {
                return NotFound($"stan sa indeksom {id} nije pronadjen!");

            }
        }

        [HttpPost]
        [Route("/api/izmeniStan")]

        public String EditStan(Stan stan)
        {
            var pomOglas = _stanData.GetStan(stan.idStana);

            if (pomOglas != null)
            {
                stan.idStana = pomOglas.idStana;
                _stanData.EditStan(stan);

            }
            return "OK";
        }
    }
}
