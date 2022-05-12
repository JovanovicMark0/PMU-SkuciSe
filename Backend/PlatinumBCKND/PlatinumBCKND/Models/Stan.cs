using System;
using System.Collections.Generic;
using System.Linq;
using System.ComponentModel.DataAnnotations;
using System.Threading.Tasks;

namespace PlatinumBCKND.Models
{
    public class Stan
    {
        [Key]
        public Guid idStana { get; set; }
        public Guid idVlasnika { get; set; }
        public int povrsina { get; set; }
        public String grad { get; set; }
        public String adresa { get; set; }
        public int brojSoba { get; set; }
        public int idTipaStana { get; set; }
        public int idStanjaObjekta { get; set; }
        public int idNamestenosti { get; set; }
        public int idGradnje { get; set; }
        public int sprat { get; set; }
        public int terasa { get; set; }
        public int telefon{get;set;}
        public int internet{get;set;}
        public int KATV{get;set;}
        public int interfon{get;set;}
        public int lift{get;set;}
        public int klima{get;set;}
        public int grejanje{get;set;}
        public int parking{get;set;}
        public int videoNadzor{get;set;}
    }
}
