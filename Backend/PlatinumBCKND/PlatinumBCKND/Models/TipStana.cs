using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Models
{
    public class TipStana
    {
        [Key]
        public int idTipa { get; set; }
        public String tipStana { get; set; }
    }
}
