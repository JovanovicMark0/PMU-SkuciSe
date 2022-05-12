using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Models
{
    public class Obavestenje
    {   
        [Key]
        public Guid idObavestenja { get; set; }
        public Guid idOglasa { get; set; }
        public Guid idPosiljaoca { get; set; }
        public Guid idPrimaoca { get; set; }
        public String datum { get; set; }
        public int prihvacen { get; set; } //-1 odbijen ,  0 ceka , 1 potvrdjen  
        public String usernamePosiljaoca { get; set; }
        public String naslovOglasa { get; set; }
    }
}
