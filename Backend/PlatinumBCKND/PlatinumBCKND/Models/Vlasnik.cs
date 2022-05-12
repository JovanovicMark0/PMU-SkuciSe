using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Models
{
    public class Vlasnik
    {
        [Key]
        public Guid idVlasnika { get; set; }
        public String username { get; set; }
        public String punoIme { get; set; }
        public String brojTelefona { get; set; }
        public int brojOglasa { get; set; }
        public int zbirSvihOcena { get; set; }
        public int brojOcena { get; set; }

        public String email { get; set; }

        public String password { get; set; }
    }
}
