using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.OglasiData
{
    public class MockObavestenjeData : IObavestenjeData
    {
        private OglasContext _oglasContext;

        public MockObavestenjeData(OglasContext oglasContext)
        {
            _oglasContext = oglasContext;
        }


        public List<Obavestenje> GetSva(String username)
        {
            List<Obavestenje> oglasiP = _oglasContext.Obavestenje.ToList();
            Vlasnik v = _oglasContext.Vlasnik.Where(v => v.username.Equals(username)).FirstOrDefault();
            List<Obavestenje> obavestenjaKorisnika = new List<Obavestenje>();
            if (v != null) {
                oglasiP.Reverse();
                
                foreach (Obavestenje o in oglasiP)
                {
                    if (o.idPrimaoca.Equals(v.idVlasnika) || o.idPosiljaoca.Equals(v.idVlasnika))
                    {
                        obavestenjaKorisnika.Add(o);
                    }
                }
            }
            return obavestenjaKorisnika;
        }

      

        public Obavestenje GetObavestenje(Guid idO)
        {
            Obavestenje o = _oglasContext.Obavestenje.Where(o => o.idObavestenja.Equals(idO)).FirstOrDefault();
            return o;
        }

        public void DeleteObavestenje(Obavestenje o)
        {
            _oglasContext.Obavestenje.Remove(o);
            _oglasContext.SaveChanges();
        }

        public void DeleteObavestenjeDelOglas(Guid idOglasa)
        {
            List<Obavestenje> obavestenja = _oglasContext.Obavestenje.ToList();
            foreach (var v in obavestenja)
            {
                if (v.idOglasa.Equals(idOglasa))
                    _oglasContext.Obavestenje.Remove(v);

            }
            _oglasContext.SaveChanges();
        }
        

        public Obavestenje EditObavestenje(Obavestenje o)
        {
            Obavestenje v = _oglasContext.Obavestenje.Where(p => p.idObavestenja == o.idObavestenja).FirstOrDefault();
            if (v != null)
            {
                v.prihvacen = o.prihvacen;
                _oglasContext.Obavestenje.Update(v);
                _oglasContext.SaveChanges();
                Console.WriteLine("EDITOVANO OBAVESTENJE");

            }
            return v;
        }


        public Obavestenje AddObavestenje(Obavestenje o)
        {
            o.idObavestenja = Guid.NewGuid();

            o.prihvacen = 0;
            
            _oglasContext.Obavestenje.Add(o);
            _oglasContext.SaveChanges();
           
            return o;
        }

        public Boolean GetNew(Guid idV)
        {
            Boolean flag = false;
            List<Obavestenje> oglasiP = _oglasContext.Obavestenje.ToList();
            Vlasnik v = _oglasContext.Vlasnik.Where(v => v.idVlasnika.Equals(idV)).FirstOrDefault();
            if (v != null)
            {
                oglasiP.Reverse();

                foreach (Obavestenje o in oglasiP)
                {
                    if (o.idPrimaoca.Equals(v.idVlasnika) && o.prihvacen == 0)
                    {
                        flag = true;
                        break;
                    }
                }
            }
            return flag;
        }
    }
}