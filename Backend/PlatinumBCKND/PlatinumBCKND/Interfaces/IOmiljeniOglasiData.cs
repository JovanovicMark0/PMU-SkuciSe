using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Interfaces
{
    public interface IOmiljeniOglasiData
    {
        List<Oglas> GetOmiljeniOglasi(String username);
        OmiljeniOglas AddOmiljeniOglas(Guid idO, Guid idV);

        void DeleteOmiljeniOglas(Guid idO, Guid idV);

        void DeleteOmiljeniOglasDelOglas(Guid idO);
    }
}