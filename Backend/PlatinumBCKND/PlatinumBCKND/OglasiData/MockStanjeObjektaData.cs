using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.OglasiData
{
    public class MockStanjeObjektaData : IStanjeObjektaData
    {
        private OglasContext _oglasContext;
        List<StanjeObjekta> stanja = new List<StanjeObjekta>();

        public MockStanjeObjektaData(OglasContext oglasContext)
        {
            _oglasContext = oglasContext;
            stanja = _oglasContext.StanjeObjekta.ToList();
        }
       

        public List<StanjeObjekta> GetStanjeObjekta()
        {
            return stanja;
        }

        public StanjeObjekta GetStanjeObjekta(int id)
        {
            return stanja.SingleOrDefault(x => x.idStanja == id);
        }
    }
}
