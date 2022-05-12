using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.OglasiData
{
    public class MockOglasData : IOglasiData
    {
        private IVlasnikData iVlasnik;
        private IStanData _iStan;
        private IObavestenjeData _iObavestenje;
        private OglasContext _oglasContext;
        List<Oglas> oglasi = new List<Oglas>();
        List<Oglas> oglasiKorisnika = new List<Oglas>();
        public MockOglasData(OglasContext oglasContext , IVlasnikData ivl, IStanData iStan, IObavestenjeData iObavestenje)
        {
            iVlasnik = ivl;
            _iObavestenje = iObavestenje;
            _oglasContext = oglasContext;
            _iStan = iStan;
            var pomOglasi = _oglasContext.Oglas.ToList();
            
            
            foreach (Oglas Oglas in pomOglasi)
            {
                if(Oglas.datumIsteka > DateTime.Now)
                {
                    oglasi.Add(Oglas);
                }
                else
                {
                    //DeleteOglas(Oglas);
                }
            }
        }


        public List<Oglas> GetOglaseKorisnika(Guid id)
        {
            List<Oglas> oglasiP = _oglasContext.Oglas.ToList();
             oglasiP.Reverse();
            foreach(Oglas o in oglasiP)
            {
                if(o.idVlasnika.Equals(id))
                {
                    oglasiKorisnika.Add(o);
                }
            }
            return oglasiKorisnika;
        }

        public Oglas AddOglas(Oglas oglas)
        {
            oglas.idOglasa = Guid.NewGuid();
            DateTime datumI = DateTime.Now;
            datumI = datumI.AddDays(15);
            oglas.datumIsteka = datumI;
            oglasi.Add(oglas);
            _oglasContext.Oglas.Add(oglas);
            _oglasContext.SaveChanges();
            Vlasnik v = new Vlasnik();
            v = iVlasnik.GetVlasnik(oglas.idVlasnika);
            v.brojOglasa++;
            iVlasnik.EditVlasnik(v);
            _oglasContext.SaveChanges();
            return oglas;
        }

        public void DeleteOglas(Oglas oglas)
        {
            oglasi.Remove(oglas);
            _oglasContext.Oglas.Remove(oglas);
            _oglasContext.SaveChanges();

            Vlasnik v = new Vlasnik();
            v = iVlasnik.GetVlasnik(oglas.idVlasnika);
            v.brojOglasa--;
            iVlasnik.EditVlasnik(v);
            _oglasContext.SaveChanges();

            _iObavestenje.DeleteObavestenjeDelOglas(oglas.idOglasa);

        }

     

        public Oglas EditOglas(Oglas oglas)
        {
            Oglas v = _oglasContext.Oglas.Where(p => p.idOglasa == oglas.idOglasa).FirstOrDefault();
            if (v != null)
            {
                v.naslovOglasa = oglas.naslovOglasa;
                v.opisOglasa = oglas.opisOglasa;
                v.cena = oglas.cena;
                v.prodajaDaNe = oglas.prodajaDaNe;
                _oglasContext.Oglas.Update(v);
                _oglasContext.SaveChanges();
                Console.WriteLine("EDITOVAN");

            }
            return v;
        }

        public List<Oglas> GetOglas()
        {
            List<Oglas> oglasi = _oglasContext.Oglas.ToList();
            oglasi.Reverse();
            return oglasi;
        }

        public Oglas GetOglas(Guid id)
        {
            Oglas o = _oglasContext.Oglas.Where(o => o.idOglasa.Equals(id)).FirstOrDefault();
            if (o != null)
            {
                o.brojPregleda++;
                _oglasContext.SaveChanges();
                Console.WriteLine($"Povecan broj pregleda > {o.brojPregleda} ,  oglasa> {o.idOglasa} ");
            }
            if(o == null)
            {
                Console.WriteLine("NIJE PRONADJEN OGLAS");
            }
            return o;
        }
        


       
        
    }
}
