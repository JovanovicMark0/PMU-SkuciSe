using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Models
{
    public class OglasFull: Oglas
    {
        public String nazivGrada { get; set; }
        public String tipGradnje { get; set; }
        public String namestenost { get; set; }
        public String stanje { get; set; }
        public String tipStana { get; set; }
        public Stan stan { get; set; }
    }
}
