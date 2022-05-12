using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.OglasiData
{
   public interface IOglasiData
    {
        List<Oglas> GetOglas();
        List<Oglas> GetOglaseKorisnika(Guid id);

        Oglas GetOglas(Guid id);

        Oglas AddOglas(Oglas oglas);

        void DeleteOglas(Oglas oglas);

        Oglas EditOglas(Oglas oglas);

        

    }
}
