using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.ComponentModel.DataAnnotations;

namespace PlatinumBCKND.Models
{
    public class StanjeObjekta
    {
        [Key]
        public int idStanja { get; set; }
        public String stanje { get; set; }
    }
}
