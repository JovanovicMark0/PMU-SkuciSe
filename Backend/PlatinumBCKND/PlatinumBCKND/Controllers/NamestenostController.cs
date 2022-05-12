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
    public class NamestenostController : ControllerBase
    {
        private INamestenostData _namestenostData;
        public NamestenostController(INamestenostData namestenostData)
        {
            _namestenostData = namestenostData;
        }

        [HttpGet]
        [Route("/api/sveNamestenosti")]
        public IActionResult GetNamestenost()
        {
            return Ok(_namestenostData.GetNamestenost());
        }

        [HttpGet]
        [Route("/api/namestenost/{id}")]
        public IActionResult GetNamestenost(int id)
        {
            var namestenost = _namestenostData.GetNamestenost(id);

            if (namestenost != null)
            {
                return Ok(namestenost);
            }
            else
            {
                return NotFound($"namestenost sa indeksom {id} nije pronadjen!");
            }
        }


    }
}
