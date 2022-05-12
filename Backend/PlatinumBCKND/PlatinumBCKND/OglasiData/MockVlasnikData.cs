using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.OglasiData
{

    public class MockVlasnikData : IVlasnikData
    {
        private OglasContext _oglasContext;
        
        List<Vlasnik> useri = new List<Vlasnik>();
        public MockVlasnikData(OglasContext oglasContext)
        {
            _oglasContext = oglasContext;
            useri = _oglasContext.Vlasnik.ToList();

        }



        public Vlasnik AddVlasnik(Vlasnik vlasnik)
        {
            vlasnik.idVlasnika = Guid.NewGuid();
            _oglasContext.Vlasnik.Add(vlasnik);
            _oglasContext.SaveChanges();
            useri.Add(vlasnik);
            return vlasnik;
        }


        public void DeleteVlasnik(Vlasnik vlasnik)
        {
            useri.Remove(vlasnik);
        }

        public Vlasnik EditVlasnik(Vlasnik vlasnik)
        {
            Vlasnik v = _oglasContext.Vlasnik.Where(p => p.idVlasnika == vlasnik.idVlasnika).FirstOrDefault();
            if (v != null)
            {
                Console.WriteLine("VLASNIK > " + vlasnik.punoIme);
                v.punoIme = vlasnik.punoIme;
                //v.username = vlasnik.username;
                v.brojTelefona = vlasnik.brojTelefona;
                /*v.brojOglasa = vlasnik.brojOglasa;
                v.zbirSvihOcena = vlasnik.zbirSvihOcena;
                v.brojOcena = vlasnik.brojOcena;
                v.email = vlasnik.email;*/
                _oglasContext.SaveChanges();
                Console.WriteLine("EDITOVAN Vlasnik");
            }
            return v;
        }


        public Vlasnik GetVlasnikU(String username)
        {
            var v = useri.SingleOrDefault(x => x.username.Equals(username));
            if(v == null)
                v = useri.SingleOrDefault(x => x.email.Equals(username));
            return v;
        }
        public Vlasnik GetVlasnik(Guid id)
        {
            return useri.SingleOrDefault(x => x.idVlasnika == id);
        }
    }
}