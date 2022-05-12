using PlatinumBCKND.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Interfaces
{
    public interface ITipStanaData
    {
        List<TipStana> GetTipStana();

        TipStana GetTipStana(int id);

    }
}
