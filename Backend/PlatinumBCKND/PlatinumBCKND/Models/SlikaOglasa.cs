using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Models
{
    public class SlikaOglasa
    {
        [Key]
        public Guid idSlike { get; set; }
        public Guid idOglasa { get; set; }
        public String slika { get; set; }
    }
}
