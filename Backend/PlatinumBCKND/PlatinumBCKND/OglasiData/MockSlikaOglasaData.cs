using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.OglasiData
{
    public class MockSlikaOglasaData : ISlikaOglasaData
    {
        private OglasContext _oglasContext;

        public MockSlikaOglasaData(OglasContext oglasContext) 
        {
            _oglasContext = oglasContext;
        }

        public List<SlikaOglasa> AddSlikeOglasa(List<SlikaOglasa> slike)
        {
            
            foreach (SlikaOglasa slika in slike)
            {
                
                if (slika.idOglasa != null && slika.slika != null)
                {
                    
                    slika.idSlike = Guid.NewGuid();
                    _oglasContext.SlikaOglasa.Add(slika);   
                }
            }
            _oglasContext.SaveChanges();
            return slike;
        }

        public void DeleteSlikeOglasa(Guid idOglasa)
        {
            List<SlikaOglasa> slike = _oglasContext.SlikaOglasa.ToList();
            foreach(var v in slike)
            {
                if(v.idOglasa.Equals(idOglasa))
                     _oglasContext.SlikaOglasa.Remove(v);
                
            }
            _oglasContext.SaveChanges();
        }

        
        public List<SlikaOglasa> GetSlikaOglasa()
        {
            var slike = _oglasContext.SlikaOglasa.ToList();
            Boolean flag = true;
            List<SlikaOglasa> slikePom = new List<SlikaOglasa>();
            foreach(SlikaOglasa o in slike)
            {
                flag = true;
                foreach(SlikaOglasa oPom in slikePom)
                {
                    if(o.idOglasa.Equals(oPom.idOglasa))
                    {
                        flag = false;
                        break;
                    }
                }
                if(flag)
                    slikePom.Add(o);
            }
           
            return slikePom;
        }

        public List<SlikaOglasa> GetSveSlikeOglasa(Guid idOglasa)
        {
            List<SlikaOglasa> slike = _oglasContext.SlikaOglasa.ToList();
            List<SlikaOglasa> slikeResult = new List<SlikaOglasa>();
            foreach(SlikaOglasa s in slike)
            {
                if(s.idOglasa.Equals(idOglasa))
                {
                    slikeResult.Add(s);
                }
            }
            return slikeResult;
        }
    }
}
