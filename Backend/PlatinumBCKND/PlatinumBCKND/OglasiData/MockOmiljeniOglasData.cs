using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.OglasiData
{
    public class MockOmiljeniOglasData : IOmiljeniOglasiData
    {
        private OglasContext _oglasContext;
        private IOglasiData _iOglasData;
        List<Oglas> oglasi = new List<Oglas>();
        List<OmiljeniOglas> oglasiOM = new List<OmiljeniOglas>();
        List<Vlasnik> vlasnici = new List<Vlasnik>();
        public MockOmiljeniOglasData(OglasContext oglasContext, IOglasiData iOglasData)
        {
            _oglasContext = oglasContext;
            _iOglasData =  iOglasData; 
            oglasi = _oglasContext.Oglas.ToList();
            vlasnici = _oglasContext.Vlasnik.ToList();
            var pomOMOglasi = _oglasContext.OmiljeniOglas.ToList();
            foreach (OmiljeniOglas o in pomOMOglasi)
            {
                oglasiOM.Add(o);
            }
        }
        public Oglas GetOglas(Guid id)
        {
            return oglasi.SingleOrDefault(x => x.idOglasa == id);
        }

        public List<Oglas> GetOmiljeniOglasi(String username)
        {
            List<OmiljeniOglas> pomO = new List<OmiljeniOglas>();
            List<Oglas> pomOg = new List<Oglas>();
            Vlasnik v = new Vlasnik();
            foreach (OmiljeniOglas o in _oglasContext.OmiljeniOglas.ToList())
            {
                v = vlasnici.Single(x => x.idVlasnika.Equals(o.idVlasnika));
                if (v.username.Equals(username))
                {
                    pomOg.Add(GetOglas(o.idOglasa));
                    pomO.Add(o);
                }
            }
           /* foreach(Oglas o in pomOg)
            {
                if(o != null)
                    Console.WriteLine(o.idOglasa + ", " + o.idVlasnika);
            }*/

            return pomOg;
        }

        public OmiljeniOglas AddOmiljeniOglas(Guid idO, Guid idV)
        {
            int flag = 0;
            
            foreach (OmiljeniOglas om in _oglasContext.OmiljeniOglas.ToList())
            {
                if (om.idOglasa.Equals(idO) && om.idVlasnika.Equals(idV))
                {
                    flag = 1;
                    break;
                }
            }
            OmiljeniOglas o = new OmiljeniOglas();
            if (flag == 0)
            {
                o.idOmiljenogOglasa = Guid.NewGuid();
                o.idOglasa = idO;
                o.idVlasnika = idV;
                oglasiOM.Add(o);
                _oglasContext.OmiljeniOglas.Add(o);
                _oglasContext.SaveChanges();

                Oglas s = _iOglasData.GetOglas(o.idOglasa);
                if(s != null)
                {
                    s.brojLajkova++;
                    _iOglasData.EditOglas(s);
                }
                Console.WriteLine("Oglas dodat u omiljene o -> " + idO + ", v->" + idV);
            }
            else
                Console.WriteLine("Oglas postoji u omiljenim o -> " + idO + ", v->" + idV);
            return o;
        }

        public void DeleteOmiljeniOglas(Guid idO, Guid idV)
        {
            foreach (OmiljeniOglas o in _oglasContext.OmiljeniOglas.ToList())
            {
                if (o.idVlasnika == idV && o.idOglasa == idO)
                {
                    oglasiOM.Remove(o);
                    _oglasContext.OmiljeniOglas.Remove(o);
                    _oglasContext.SaveChanges();

                    Oglas s = _iOglasData.GetOglas(idO);
                    if (s != null)
                    {
                        s.brojLajkova--;
                        _iOglasData.EditOglas(s);
                    }
                    Console.WriteLine("Oglas uklonjen iz omiljenih o -> " + idO + ", v->" + idV);
                }
            }
        }

        public void DeleteOmiljeniOglasDelOglas(Guid idO)
        {
            foreach (OmiljeniOglas o in _oglasContext.OmiljeniOglas.ToList())
            {
                if (o.idOglasa == idO)
                {
                    oglasiOM.Remove(o);
                    _oglasContext.OmiljeniOglas.Remove(o);
                    _oglasContext.SaveChanges();
                    Console.WriteLine("Oglas uklonjen iz omiljenih o -> " + idO);
                }
            }
        }

    }
}
