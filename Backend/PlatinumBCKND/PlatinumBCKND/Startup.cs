using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.HttpsPolicy;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using PlatinumBCKND.Hubs;
using PlatinumBCKND.Interfaces;
using PlatinumBCKND.Models;
using PlatinumBCKND.OglasiData;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PlatinumBCKND
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
            
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            
            //services.AddRazorPages();
            services.AddControllers();

            //services.AddEntityFrameworkSqlite().AddDbContext<OglasContext>();
            // services.AddDbContextPool<OglasContext>(opstion=> OptionsBuilderConfigurationExtensions.UseSqliteServer())

            services.AddScoped<IOglasiData, MockOglasData>();
            services.AddScoped<IGradData, MockGradData>();
            services.AddScoped<IGradnjaData, MockGradnjaData>();
            services.AddScoped<INamestenostData, MockNamestenostData>();
            services.AddScoped<IStanData, MockStanData>();
            services.AddScoped<IStanjeObjektaData, MockStanjeObjektaData>();
            services.AddScoped<ITipStanaData, MockTipStanaData>();
            services.AddScoped<IVlasnikData, MockVlasnikData>();
            services.AddScoped<IImageStorage, MockImageData>();
            services.AddScoped<IOmiljeniOglasiData, MockOmiljeniOglasData>();
            services.AddScoped<IMessage, MockMessageData>();
            services.AddScoped<IOcenaData, MockOcenaData>();
            services.AddScoped<ISlikaKorisnikaData, MockSlikaKorisnikaData>();
            services.AddScoped<ISlikaOglasaData, MockSlikaOglasaData>();
            services.AddScoped<IObavestenjeData, MockObavestenjeData>();

            services.AddDbContext<OglasContext>(options => options.UseSqlite(Configuration.GetConnectionString("OglasConnStr")));

            services.AddSignalR();

            services.AddCors(options =>
            {
                options.AddPolicy("_allowAnyOrigin",
                                  builder =>
                                  {
                                      builder.AllowAnyOrigin().AllowAnyHeader().AllowAnyMethod();
                                  });
            });

        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }
            /* else
             {
                 app.UseExceptionHandler("/Error");
                 // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
                 app.UseHsts();
             }*/

            app.UseRouting();
            app.UseStaticFiles();
            app.UseCors("_allowAnyOrigin");
            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                //endpoints.MapRazorPages();
                endpoints.MapControllers();
            });

            //CHAT START
            app.UseEndpoints(endpoints =>
            {
                endpoints.MapHub<ChatHub>("/chatter");
            });
            //CHAT END

        }
    }
}

