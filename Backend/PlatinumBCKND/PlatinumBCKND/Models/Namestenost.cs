using System;
using System.Collections.Generic;
using System.Linq;
using System.ComponentModel.DataAnnotations;
using System.Threading.Tasks;

namespace PlatinumBCKND.Models
{
    public class Namestenost
    {
        [Key]
        public int idNamestenosti { get; set; }
        public String namestenost { get; set; }
    }
}
