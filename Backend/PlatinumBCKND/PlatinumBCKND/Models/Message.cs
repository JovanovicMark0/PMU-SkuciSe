using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Models
{
    public class Message
    {
        public Guid ID { get; set; }
        public String messageContent { get; set; }
        public DateTime date { get; set; }
        public String messageFrom { get; set; }
        public String messageTo { get; set; }


    }
}
