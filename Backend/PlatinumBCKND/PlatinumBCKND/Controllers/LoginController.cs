using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using PlatinumBCKND.Models;
using PlatinumBCKND.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PlatinumBCKND.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class LoginController : ControllerBase
    {
        private OglasContext _oglasContext;
        public LoginController(OglasContext cntx)
        {
            _oglasContext = cntx;
        }


        [HttpGet]
        [Route("/api/login/{username}/{password}")]
        public string Login(String username, String password)
        {
            if (_oglasContext.Login.Any(user => user.username.Equals(username)) || _oglasContext.Login.Any(user => user.email.Equals(username)))
            {
                Login user = _oglasContext.Login.Where(user => user.username.Equals(username
                    )).FirstOrDefault();
                if(user == null)
                {
                    user = _oglasContext.Login.Where(user => user.email.Equals(username
                       )).FirstOrDefault();
                }
                //calculate hash password from data of client and compare with hash in server with salt
                var pass = password;
                /*var pass = Convert.ToBase64String(Common.SaltHashPassword(
                    Encoding.ASCII.GetBytes(password),
                    Convert.FromBase64String(user.salt)));*/

                if (pass.Equals(user.password))
                {
                    Console.WriteLine("Ulogovan korisnik");
                    return JsonConvert.SerializeObject(user);
                }

                else
                {

                    Console.WriteLine("Netačna lozinka");
                    return JsonConvert.SerializeObject("Wrong Password");
                }
            }
            else
            {
                    Console.WriteLine("Nepostojeci korisnik");
                    return JsonConvert.SerializeObject("User is not existing in Database");
            }
        }
        /*public string Get([FromBody]Login value)
        {
           */
    }
}
