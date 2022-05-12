using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.ComponentModel.DataAnnotations;


namespace PlatinumBCKND.Models
{
    public class Gradnja
    {
        [Key]
        public int idGradnje { get; set; }
        public String tipGradnje { get; set; }
    }
}
