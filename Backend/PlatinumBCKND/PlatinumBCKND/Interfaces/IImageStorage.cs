using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Interfaces
{
   public  interface IImageStorage
    {
        void Upload(IFormFile formFile);
    }
}
