using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;

namespace ImageUploadDemo.Controllers
{
    [System.Web.Http.Route("api/[controller]")]
    public class ImagesController : ControllerBase
    {
        private readonly IHostingEnvironment _hostingEnv;
        private readonly OglasContext _context;
        private readonly IImageStorage _image;
        

        public ImagesController(OglasContext context, IImageStorage image,  IHostingEnvironment hostingEnv)
        {
            _hostingEnv = hostingEnv;
            _context = context;
            _image = image;
        }
        public IActionResult Get()
        {
            return Ok("OK");
        }


        [Microsoft.AspNetCore.Mvc.HttpPost]
        [Microsoft.AspNetCore.Mvc.Route("/api/image")]
        public ActionResult<string> Upload()
        {
            try
            {
                //Console.WriteLine("Dodajem slike za oglas > " + idOglasa);
                var files = HttpContext.Request.Form.Files;
                if(files != null && files.Count > 0)
                {
                    foreach(var file in files)
                    {
                        var guid = Guid.NewGuid();
                        FileInfo fi = new FileInfo(file.FileName);
                        String idOglasa = fi.Name;
                        var newfilename = idOglasa + "_" + guid + fi.Extension;
                        var path = Path.Combine("", _hostingEnv.ContentRootPath + "\\Images\\" + newfilename);
                        byte[] blob = new byte[file.Length];
                        using (var stream = new FileStream(path, FileMode.Create))
                        {
                            file.CopyTo(stream);
                            stream.Read(blob, 0, (int)stream.Length);
                            //stream.Dispose();
                        }
                        //byte[] blob = new byte[stream.Length];
                        //stream.Read(blob, 0, (int)stream.Length);

                        Image image = new Image();
                        image.putanja = path;
                        image.idSlike = guid;
                        //image.idOglasa = idOglasa;
                        image.slika = blob;
                        _context.Image.Add(image);
                        _context.SaveChanges();
                    }
                    return "Saved Successfully";
                }
                else
                {
                    return "Select Files";
                }
            }
            catch(Exception ex)
            {
                return ex.Message;
            }
        }

        [Microsoft.AspNetCore.Mvc.HttpGet]
        [Microsoft.AspNetCore.Mvc.Route("/api/image")]
        public ActionResult<List<Image>> GetImage()
        {
            var result = _context.Image.ToList();
            return result;
        }

        [Microsoft.AspNetCore.Mvc.HttpGet]
        [Microsoft.AspNetCore.Mvc.Route("/api/slike/{id}")]
        public List<Image> GetImages(String id)
        {
            Console.WriteLine("Trazim slike za oglas > " + id);
            var result = _context.Image.ToList();
            List<Image> imgs = new List<Image>();
            foreach (Image i in result)
            {
                if (i.idOglasa.Equals(id))
                    imgs.Add(i);
            }
            return imgs;
        }

        [HttpGet]
        [Route("/api/slikeOglasa/{id}")]
        public List<Image> GetImagesFrom(Guid id)
        {
            Console.WriteLine("Trazim slike za oglas > " + id);
            var result = _context.Image.ToList();
            List<Image> imgs = new List<Image>();
            foreach (Image i in result)
            {
                if (i.idOglasa.Equals(id))
                    imgs.Add(i);
            }
            return imgs;
        }
    }
}