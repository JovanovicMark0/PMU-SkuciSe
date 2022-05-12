using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Models
{
    public class Ocena
    {
        [Key]
        public Guid idOcene { get; set; }
        public Guid idOcenjenog { get; set; }
        public Guid idOcenjivaca { get; set; }
        public int ocena { get; set; }
        public string komentar { get; set; }
        public string naslov{get;set;}
    }
}
