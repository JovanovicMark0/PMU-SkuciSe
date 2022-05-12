using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Interfaces
{
    public interface ISlikaKorisnikaData
    {
        SlikaKorisnika GetSlikaKorisnika(Guid idKorisnika);

        SlikaKorisnika EditSlikaKorisnika(SlikaKorisnika slika);

        SlikaKorisnika AddSlikaKorisnika(SlikaKorisnika slika);

    }
}
