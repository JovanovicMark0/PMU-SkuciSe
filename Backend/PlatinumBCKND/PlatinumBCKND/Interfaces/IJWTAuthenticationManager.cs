using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND.Interfaces
{
    public interface IJWTAuthenticationManager
    {
        string Authenticate(string Email, string password);
    }
}
