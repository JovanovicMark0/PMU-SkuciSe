using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Interfaces
{
   public interface IStanjeObjektaData
   {
        List<StanjeObjekta> GetStanjeObjekta();

        StanjeObjekta GetStanjeObjekta(int id);
    }
}
