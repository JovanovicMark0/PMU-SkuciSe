using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using PlatinumBCKND.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class StanjeObjektaController : ControllerBase
    {
        private IStanjeObjektaData _stanjeData;
        public StanjeObjektaController(IStanjeObjektaData stanjeData)
        {
            _stanjeData = stanjeData;
        }

        [HttpGet]
        [Route("/api/svaStanja")]
        public IActionResult GetStanjeObjekta()
        {
            return Ok(_stanjeData.GetStanjeObjekta());
        }

        [HttpGet]
        [Route("/api/stanje/{id}")]
        public IActionResult GetGrad(int id)
        {
            var stanje = _stanjeData.GetStanjeObjekta(id);

            if (stanje != null)
            {
                return Ok(stanje);
            }
            else
            {
                return NotFound($"stanje sa indeksom {id} nije pronadjen!");
            }
        }


    }
}
