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
    public class RegisterController : ControllerBase
    {
        private OglasContext _oglasContext;
        private List<Login> logins = new List<Login>();
        public RegisterController(OglasContext cntx)
        {
            _oglasContext = cntx;
            foreach (Login l in _oglasContext.Login)
            {
                logins.Add(l);
            }
        }
        [HttpPost]
        [Route("/api/register")]
        public Vlasnik Post([FromBody] Vlasnik value)
        {
            if (_oglasContext.Login.Where(user => user.username.Equals(value.username)).FirstOrDefault() != null || _oglasContext.Login.Where(user => user.email.Equals(value.email)).FirstOrDefault() != null)
            {

                return null;
            }
            else
            {
                Login user = new Login();
                user.idUser = new Guid();
                user.username = value.username;
                user.email = value.email;
                user.salt = Convert.ToBase64String(Common.GetRandomSalt(16));
                /*user.password = Convert.ToBase64String(Common.SaltHashPassword(
                    Encoding.ASCII.GetBytes(value.password),
                    Convert.FromBase64String(user.salt)));*/
                user.password = value.password;
                //add to db
                try
                {
                    Vlasnik v = new Vlasnik();
                    v.idVlasnika = user.idUser;
                    v.username = user.username;
                    v.email = user.email;
                    v.brojOcena = 0;
                    v.brojOglasa = 0;
                    v.brojTelefona = value.brojTelefona;
                    v.punoIme = value.punoIme;
                    v.zbirSvihOcena = 0;
                    _oglasContext.Vlasnik.Add(v);
                    _oglasContext.SaveChanges();
                    Console.WriteLine("DODAT KORISNIK");
                    logins.Add(user);
                    _oglasContext.Login.Add(user);
                    _oglasContext.SaveChanges();
                    Console.WriteLine("DODAT LOGIN KORISNIK");
                    return v;
                }
                catch (Exception ex)
                {
                    return null;
                }
            }
        }

        [HttpPost]
        [Route("/api/zaboravljenaLozinka")]
        public IActionResult editRegister([FromBody] Vlasnik value)
        {
            if (_oglasContext.Login.Where(user => user.username.Equals(value.username)).FirstOrDefault() == null || _oglasContext.Login.Where(user => user.email.Equals(value.email)).FirstOrDefault() == null)
            {
                return Ok(false);
            }
            else
            {
                Login user = _oglasContext.Login.Where(user => user.username.Equals(value.username)).FirstOrDefault();
                if (user.email != value.email)
                    return Ok(false);
                else
                {
                    user.salt = Convert.ToBase64String(Common.GetRandomSalt(16));
                    /*user.password = Convert.ToBase64String(Common.SaltHashPassword(
                        Encoding.ASCII.GetBytes(value.password),
                        Convert.FromBase64String(user.salt)));*/
                    user.password = value.password;
                    //add to db
                    try
                    {
                        _oglasContext.SaveChanges();
                        Console.WriteLine("IZMENJENA SIFRA");
                        return Ok(true); 
                    }
                    catch (Exception ex)
                    {
                        return Ok(false);
                    }
                }
            }
        }

        [HttpGet]
        [Route("/api/izmeniSifru/{username}/{password}/{passwordNew}")]
        public string Login(String username, String password, String passwordNew)
        {
            Console.WriteLine("u > " + username + " > p> " + password + " . pn > " + passwordNew);
            if (_oglasContext.Login.Any(user => user.username.Equals(username)) || _oglasContext.Login.Any(user => user.email.Equals(username)))
            {
                Login user = _oglasContext.Login.Where(user => user.username.Equals(username)).First();

                //calculate hash password from data of client and compare with hash in server with salt
                /*var client_post_hash_pass = Convert.ToBase64String(
                    Common.SaltHashPassword(
                        Encoding.ASCII.GetBytes(password),
                        Convert.FromBase64String(user.salt)));*/
                //var client_post_hash_pass = password;
                if (password.Equals(user.password))
                {
                    
                    user.salt = Convert.ToBase64String(Common.GetRandomSalt(16));
                    /*user.password = Convert.ToBase64String(Common.SaltHashPassword(
                        Encoding.ASCII.GetBytes(passwordNew),
                        Convert.FromBase64String(user.salt)));*/
                    user.password = passwordNew;
                    //add to db
                    try
                    {
                        _oglasContext.SaveChanges();
                        Console.WriteLine("IZMENJENA SIFRA");
                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine("NIJE IZMENJENA SIFRA");
                    }
                    return JsonConvert.SerializeObject("IZMENJENA SIFRA");
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
    }
}
