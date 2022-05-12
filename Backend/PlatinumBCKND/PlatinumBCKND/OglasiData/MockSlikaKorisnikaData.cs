using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.OglasiData
{
    public class MockSlikaKorisnikaData : ISlikaKorisnikaData
    {
        private OglasContext _oglasContext;

        List<SlikaKorisnika> slikeKorisnika = new List<SlikaKorisnika>();
        public MockSlikaKorisnikaData(OglasContext oglasContext)
        {
            _oglasContext = oglasContext;
            slikeKorisnika = _oglasContext.SlikaKorisnika.ToList();

        }
       
        public SlikaKorisnika GetSlikaKorisnika(Guid idKorisnika)
        {
            return slikeKorisnika.SingleOrDefault(x => x.idKorisnika.Equals(idKorisnika));
        }

        public SlikaKorisnika EditSlikaKorisnika(SlikaKorisnika slika)
        {
            SlikaKorisnika v = _oglasContext.SlikaKorisnika.Where(p => p.idKorisnika == slika.idKorisnika).FirstOrDefault();
            if (v != null)
            {
                v.slika = slika.slika;
                _oglasContext.SaveChanges();
            }
            return v;
        }

        public SlikaKorisnika AddSlikaKorisnika(SlikaKorisnika slika)
        {
            if (slika.idKorisnika != null && slika.slika != null)
            {
                _oglasContext.SlikaKorisnika.Add(slika);
                _oglasContext.SaveChanges();
                slikeKorisnika.Add(slika);
            }
            return slika;
        }
    }
}