using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Models
{
    public class SlikaKorisnika
    {
        [Key]
        public Guid idKorisnika { get; set; }
        public String slika { get; set; }

        //public byte[] slikaB { get; set; }
    }
}