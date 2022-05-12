using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Interfaces
{
    public interface IOcenaData
    {
        List<Ocena> GetOceneKorisnika(Guid idK);
        Ocena AddOcena(Ocena ocena);

    }
}
