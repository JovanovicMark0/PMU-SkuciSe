using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Interfaces
{
    public interface ISlikaOglasaData
    {
        List<SlikaOglasa> GetSlikaOglasa();

        List<SlikaOglasa> GetSveSlikeOglasa(Guid idOglasa);

        List<SlikaOglasa> AddSlikeOglasa(List<SlikaOglasa> slike);

        void DeleteSlikeOglasa(Guid idOglasa);
    }
}
