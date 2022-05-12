using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.OglasiData
{
    public class MockTipStanaData : ITipStanaData
    {
        private OglasContext _oglasContext;
        List<TipStana> tipoviStanova = new List<TipStana>();

        public MockTipStanaData(OglasContext oglasContext)
        {
            _oglasContext = oglasContext;
            tipoviStanova = _oglasContext.TipStana.ToList();
        }

       
        public List<TipStana> GetTipStana()
        {
            return tipoviStanova;
        }

        public TipStana GetTipStana(int id)
        {
            return tipoviStanova.SingleOrDefault(x => x.idTipa == id);
        }
    }
}
