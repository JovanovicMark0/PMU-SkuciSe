using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Interfaces
{
    public interface IVlasnikData
    {
        Vlasnik GetVlasnikU(String username);
        Vlasnik GetVlasnik(Guid id);

        Vlasnik AddVlasnik(Vlasnik vlasnik);

        void DeleteVlasnik(Vlasnik vlasnik);

        Vlasnik EditVlasnik(Vlasnik vlasnik);
    }
}
