using Microsoft.AspNetCore.Mvc;
using PlatinumBCKND.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Controllers
{
    public class TipStanaController : Controller
    {
        private ITipStanaData _tipStana;
        public TipStanaController(ITipStanaData tipStana)
        {
            _tipStana = tipStana;
        }

        [HttpGet]
        [Route("/api/sviTipovi")]
        public IActionResult GetTipStana()
        {
            return Ok(_tipStana.GetTipStana());
        }

        [HttpGet]
        [Route("/api/tipStana/{id}")]
        public IActionResult GetTipStana(int id)
        {
            var tip = _tipStana.GetTipStana(id);

            if (tip != null)
            {
                return Ok(tip);
            }
            else
            {
                return NotFound($"tip sa indeksom {id} nije pronadjen!");
            }
        }


    }
}

