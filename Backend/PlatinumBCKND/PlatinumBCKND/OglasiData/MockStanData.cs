using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.OglasiData
{
    public class MockStanData : IStanData
    {
        private OglasContext _oglasContext;
        List<Stan> stanovi = new List<Stan>();

        public MockStanData(OglasContext oglasContext)
        {
            _oglasContext = oglasContext;
            stanovi = _oglasContext.Stan.ToList();
        }

        public Stan AddStan(Stan stan)
        {
            stan.idStana = Guid.NewGuid();
            stanovi.Add(stan);
            _oglasContext.Stan.Add(stan);
            _oglasContext.SaveChanges();
            return stan;
        }

        public void DeleteStan(Stan stan)
        {
            stanovi.Remove(stan);
            if (_oglasContext.Stan.Where(p => p.idStana == stan.idStana).FirstOrDefault() != null)
            {
                _oglasContext.Remove(stan);
                _oglasContext.SaveChanges();
            }
        }

       

        public void DeleteStanDelOglas(Guid id)
        {
            var s =_oglasContext.Stan.Where(p => p.idStana == id).FirstOrDefault();
            if(s != null)    
             DeleteStan(s);
            
        }

        public Stan EditStan(Stan stan)
        {
            Stan pom = _oglasContext.Stan.Where(p => p.idStana == stan.idStana).FirstOrDefault();
            if (pom != null)
            {
                pom.grad = stan.grad;
                pom.grejanje = stan.grejanje;
                pom.idGradnje = stan.idGradnje;
                pom.idNamestenosti = stan.idNamestenosti;
                pom.idStanjaObjekta = stan.idStanjaObjekta;
                pom.idTipaStana = stan.idTipaStana;
                pom.terasa = stan.terasa;
                pom.telefon = stan.telefon;
                pom.interfon = stan.interfon;
                pom.internet = stan.internet;
                pom.KATV = stan.KATV;
                pom.klima = stan.klima;
                pom.povrsina = stan.povrsina;
                pom.adresa = stan.adresa;
                pom.brojSoba = stan.brojSoba;
                pom.sprat = stan.sprat;
                pom.lift = stan.lift;
                pom.parking = stan.parking;
                pom.videoNadzor = stan.videoNadzor;
                _oglasContext.SaveChanges();
                Console.WriteLine("EDITOVAN Stan");

            }
           
            return pom;
        }

        public List<Stan> GetStan()
        {
            return stanovi;
        }

        public Stan GetStan(Guid id)
        {
            return stanovi.SingleOrDefault(x => x.idStana == id);
        }
    }
}
