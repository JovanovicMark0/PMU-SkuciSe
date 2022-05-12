using Azure.Storage.Blobs;
using Microsoft.AspNetCore.Http;
using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.OglasiData
{
    public class MockImageData : IImageStorage
    {
        private OglasContext _oglasContext;
        List<Image> slike = new List<Image>();
        public MockImageData(OglasContext oglasContext)
        {
            _oglasContext = oglasContext;
            slike = _oglasContext.Image.ToList();

        }
        public void Upload(IFormFile formFile)
        {
            /*slika.idSlike = Guid.NewGuid();
            slike.Add(slika);
            return slika;*/
        }
    }
}
