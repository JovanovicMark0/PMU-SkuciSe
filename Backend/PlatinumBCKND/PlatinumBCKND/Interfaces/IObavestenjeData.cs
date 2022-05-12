using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Interfaces
{
    public interface IObavestenjeData
    {
        List<Obavestenje> GetSva(String username);

        Boolean GetNew(Guid idV);
        Obavestenje AddObavestenje(Obavestenje o);
        Obavestenje GetObavestenje(Guid idO);
        void DeleteObavestenje(Obavestenje o);

        void DeleteObavestenjeDelOglas(Guid idO);
        Obavestenje EditObavestenje(Obavestenje o);
    }
}
