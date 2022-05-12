using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Models
{
    public class OmiljeniOglas
    {
        [Key]
        public Guid idOmiljenogOglasa { get; set; }
        public Guid idVlasnika { get; set; }
        
        public Guid idOglasa { get; set; }
        
    }
}
