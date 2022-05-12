using PlatinumBCKND.Controllers;
using PlatinumBCKND.Interfaces;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using PlatinumBCKND.Models;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace PlatinumBCKND.Controllers
{
    [Authorize]
    [Route("[controller]")]
    [ApiController]
    public class TokenController : ControllerBase
    {
        public readonly IJWTAuthenticationManager jwtAuthenticationManager;

        public TokenController(IJWTAuthenticationManager jwtAuthenticationManager)
        {
            this.jwtAuthenticationManager = jwtAuthenticationManager;
        }

        // GET: api/<TokenController>
        [HttpGet]
        public IEnumerable<string> Get()
        {
            return new string[] { "New Jersey", "New York" };
        }

        // GET api/<TokenController>/5
        [HttpGet("{id}")]
        public string Get(int id)
        {
            return "value";
        }

        [AllowAnonymous]
        [HttpPost("authenticate")]
        public IActionResult Authenticate([FromBody] Vlasnik userCred)
        {
            var token = jwtAuthenticationManager.Authenticate(userCred.email, userCred.password);
            if (token == null)
                return Unauthorized();
            return Ok(token);
        }

    }
}
