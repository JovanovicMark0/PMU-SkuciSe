using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.OglasiData
{
    public class MockNamestenostData : INamestenostData
    {

        private OglasContext _oglasContext;
        List<Namestenost> namestenosti = new List<Namestenost>();

        public MockNamestenostData(OglasContext oglasContext)
        {
            _oglasContext = oglasContext;
            namestenosti = _oglasContext.Namestenost.ToList();
        }

        public List<Namestenost> GetNamestenost()
        {
            return namestenosti;
        }

        public Namestenost GetNamestenost(int id)
        {
            return namestenosti.SingleOrDefault(x => x.idNamestenosti == id);
        }
    }
}
