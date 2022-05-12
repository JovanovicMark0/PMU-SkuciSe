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
    public class GradnjaController : ControllerBase
    {
        private IGradnjaData _gradnjaData;
        public GradnjaController(IGradnjaData gradnjaData)
        {
            _gradnjaData = gradnjaData;
        }

        [HttpGet]
        [Route("/api/sveGradnje")]
        public IActionResult GetGradnja()
        {
            return Ok(_gradnjaData.GetGradnja());
        }

        [HttpGet]
        [Route("/api/gradnja/{id}")]
        public IActionResult GetGradnja(int id)
        {
            var gradnja = _gradnjaData.GetGradnja(id);

            if (gradnja != null)
            {
                return Ok(gradnja);
            }
            else
            {
                return NotFound($"gradnja sa indeksom {id} nije pronadjen!");
            }
        }


    }
}
