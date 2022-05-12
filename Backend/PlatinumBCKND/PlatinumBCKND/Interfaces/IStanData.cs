using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Interfaces
{
    public interface IStanData
    {
        List<Stan> GetStan();

        Stan GetStan(Guid id);

        Stan AddStan(Stan stan);

        void DeleteStan(Stan stan);

        Stan EditStan(Stan stan);
        void DeleteStanDelOglas(Guid id);
    }
}
