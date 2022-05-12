using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.OglasiData
{
    public class MockOcenaData : IOcenaData
    {
        private IVlasnikData iVlasnik;
        private OglasContext _oglasContext;
        public MockOcenaData(OglasContext oglasContext, IVlasnikData ivl)
        {
            iVlasnik = ivl;
            _oglasContext = oglasContext;
            var pomOglasi = _oglasContext.Oglas.ToList();
        }

        public Ocena AddOcena(Ocena ocena)
        {
            ocena.idOcene = Guid.NewGuid();
            _oglasContext.Ocena.Add(ocena);

            Vlasnik v = _oglasContext.Vlasnik.Where(p => p.idVlasnika == ocena.idOcenjenog).FirstOrDefault();
            if(v != null)
            {
                v.zbirSvihOcena += ocena.ocena;
                v.brojOcena++;
            }

            _oglasContext.SaveChanges();
            return ocena;
        }

        public List<Ocena> GetOceneKorisnika(Guid idK)
        {
            /* List<Ocena> ocene = _oglasContext.Ocena.ToList();
             List<Ocena> ocenePom = new List<Ocena>();
             foreach (Ocena o in ocene)
             {
                 if (o.idOcenjenog.Equals(idK))
                 {
                     ocenePom.Add(o);
                 }
             }
             return ocenePom;*/
            Console.WriteLine(idK);
            return _oglasContext.Ocena.ToList();
        }
    }
}
