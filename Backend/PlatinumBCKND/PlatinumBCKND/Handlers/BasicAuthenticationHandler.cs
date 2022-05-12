
using PlatinumBCKND.Models;

using Microsoft.AspNetCore.Authentication;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Headers;
using System.Security.Claims;
using System.Text;
using System.Text.Encodings.Web;
using System.Threading.Tasks;

namespace PlatinumBCKND.Handlers
{
    public class BasicAuthenticationHandler : AuthenticationHandler<AuthenticationSchemeOptions>
    {
        private OglasContext _messageCntx;
        public BasicAuthenticationHandler(IOptionsMonitor<AuthenticationSchemeOptions> options, ILoggerFactory logger, UrlEncoder encoder, ISystemClock clock, OglasContext messageContext)
            : base(options, logger, encoder, clock)
        {
            _messageCntx = messageContext;
        }

        protected override async Task<AuthenticateResult> HandleAuthenticateAsync()
        {
            if (!Request.Headers.ContainsKey("Authorization"))
                return AuthenticateResult.Fail("Authorization header was not found");

            try
            {
                var authenticationHeaderValue = AuthenticationHeaderValue.Parse(Request.Headers["Authorization"]);
                var bytes = Convert.FromBase64String(authenticationHeaderValue.Parameter);
                string[] credentials = Encoding.UTF8.GetString(bytes).Split(":");
                string email = credentials[0];
                string password = credentials[1];
                Console.WriteLine(email + " " + password);

                Vlasnik user = _messageCntx.Vlasnik.Where(user => user.email == email && user.password == password).FirstOrDefault();
                if(user == null)
                    user = _messageCntx.Vlasnik.Where(user => user.username == email && user.password == password).FirstOrDefault();

                if (user == null)
                    AuthenticateResult.Fail("Invalid username or password");
                else
                {
                    var claims = new[] { new Claim(ClaimTypes.Name, user.email) };
                    var identity = new ClaimsIdentity(claims, Scheme.Name);
                    var principal = new ClaimsPrincipal(identity);
                    var ticket = new AuthenticationTicket(principal, Scheme.Name);

                    return AuthenticateResult.Success(ticket);
                }
            }
            catch (Exception)
            {
                return AuthenticateResult.Fail("Error has occured");
            }
            return AuthenticateResult.Fail("Need to implement");
        }
    }
}