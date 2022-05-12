using System;
using System.Collections.Generic;
using System.Linq;
using System.ComponentModel.DataAnnotations;
using System.Threading.Tasks;

namespace PlatinumBCKND.Models
{
    public class Grad
    {
        [Key]
        public int idGrada { get; set; }
        public String nazivGrada { get; set; }
    }
}
