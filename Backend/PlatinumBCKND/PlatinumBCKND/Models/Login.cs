using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.ComponentModel.DataAnnotations;

namespace PlatinumBCKND.Models
{
    public class Login
    {
        [Key]
        public Guid idUser { get; set; }
        public String username { get; set; }
        public String password { get; set; }
        public String salt { get; set; }

        public String email { get; set; }
    }
}
