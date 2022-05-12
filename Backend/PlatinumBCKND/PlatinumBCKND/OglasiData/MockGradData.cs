using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.OglasiData
{
    public class MockGradData : IGradData
    {
        private OglasContext _oglasContext;
        List<Grad> gradovi = new List<Grad>();

        public MockGradData(OglasContext oglasContext)
        {
            _oglasContext = oglasContext;
            gradovi = _oglasContext.Grad.ToList();
        }
        public List<Grad> GetGrad()
        {
            return gradovi;
        }

        public Grad GetGrad(int id)
        {
            return gradovi.SingleOrDefault(x => x.idGrada == id);
        }
    }
}
