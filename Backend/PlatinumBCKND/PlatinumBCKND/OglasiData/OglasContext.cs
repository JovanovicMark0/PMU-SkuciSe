using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Threading.Tasks;

namespace PlatinumBCKND.Models
{
    public class OglasContext:DbContext
    {
        public OglasContext(DbContextOptions<OglasContext> options) : base(options)
        { }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);

            builder.ApplyConfigurationsFromAssembly(Assembly.GetExecutingAssembly());
        }

        

        public OglasContext()
        {}

        public DbSet<Oglas> Oglas { get; set; }
        public DbSet<Grad> Grad { get; set; }
        public DbSet<Stan> Stan { get; set; }
        public DbSet<Gradnja> Gradnja { get; set; }
        public DbSet<Namestenost> Namestenost { get; set; }
        public DbSet<StanjeObjekta> StanjeObjekta { get; set; }
        public DbSet<TipStana> TipStana { get; set; }
        public DbSet<Vlasnik> Vlasnik { get; set; }
        public DbSet<Login> Login { get; set; }
        public DbSet<SlikaOglasa> SlikaOglasa { get; set; }
        public DbSet<SlikaKorisnika> SlikaKorisnika { get; set; }
        public DbSet<Image> Image { get; set; }
        public DbSet<OmiljeniOglas> OmiljeniOglas { get; set; }
        public DbSet<Message> Messages { get; set; }
        public DbSet<Ocena> Ocena { get; set; }

        public DbSet<Obavestenje> Obavestenje { get; set; }
    }
}
