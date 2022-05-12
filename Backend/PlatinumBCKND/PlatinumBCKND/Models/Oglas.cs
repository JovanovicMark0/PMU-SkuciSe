using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Models
{
    public class Oglas
    {
        [Key]
        public Guid idOglasa { get; set; }
        public Guid idVlasnika { get; set; }

        public Guid idStana { get; set; }
        public int cena { get; set; }
        public string naslovOglasa { get; set; }
        public string opisOglasa { get; set; }
        public DateTime datumIsteka { get; set; }
        public int prodajaDaNe{ get; set; }
        
        public int brojLajkova { get; set; }
        public int brojPregleda { get; set; }

        /*
         * 
         * [Required]
         * [MaxLenfth(50, ErrorMessage = "name can onlyu be 50 chars")
         * */
    }
}
