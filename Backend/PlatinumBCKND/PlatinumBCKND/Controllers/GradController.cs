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
    public class GradController : ControllerBase
    {
        private IGradData _gradData;
        public GradController(IGradData gradData)
        {
            _gradData = gradData;
        }

        [HttpGet]
        [Route("/api/sviGradovi")]
        public IActionResult GetGrad()
        {
            return Ok(_gradData.GetGrad());
        }

        [HttpGet]
        [Route("/api/grad/{id}")]
        public IActionResult GetGrad(int id)
        {
            var grad = _gradData.GetGrad(id);

            if (grad != null)
            {
                return Ok(grad);
            }
            else
            {
                return NotFound($"Grad sa indeksom {id} nije pronadjen!");
            }
        }

        
    }
}
