using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.OglasiData
{
    public class MockGradnjaData:IGradnjaData
    {
        private OglasContext _oglasContext;
        List<Gradnja> gradnja = new List<Gradnja>();

        public MockGradnjaData(OglasContext oglasContext)
        {
            _oglasContext = oglasContext;
            gradnja = _oglasContext.Gradnja.ToList();
        }
        public List<Gradnja> GetGradnja()
        {
            return gradnja;
        }

        public Gradnja GetGradnja(int id)
        {
            return gradnja.SingleOrDefault(x => x.idGradnje == id);
        }
    }
}
