using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace PlatinumBCKND.Migrations
{
    public partial class second : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "Grad",
                columns: table => new
                {
                    idGrada = table.Column<int>(nullable: false)
                        .Annotation("Sqlite:Autoincrement", true),
                    nazivGrada = table.Column<string>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Grad", x => x.idGrada);
                });

            migrationBuilder.CreateTable(
                name: "Gradnja",
                columns: table => new
                {
                    idGradnje = table.Column<int>(nullable: false)
                        .Annotation("Sqlite:Autoincrement", true),
                    tipGradnje = table.Column<string>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Gradnja", x => x.idGradnje);
                });

            migrationBuilder.CreateTable(
                name: "Login",
                columns: table => new
                {
                    idUser = table.Column<Guid>(nullable: false),
                    username = table.Column<string>(nullable: true),
                    password = table.Column<string>(nullable: true),
                    salt = table.Column<string>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Login", x => x.idUser);
                });

            migrationBuilder.CreateTable(
                name: "Namestenost",
                columns: table => new
                {
                    idNamestenosti = table.Column<int>(nullable: false)
                        .Annotation("Sqlite:Autoincrement", true),
                    namestenost = table.Column<string>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Namestenost", x => x.idNamestenosti);
                });

            migrationBuilder.CreateTable(
                name: "Oglas",
                columns: table => new
                {
                    idOglasa = table.Column<Guid>(nullable: false),
                    idVlasnika = table.Column<Guid>(nullable: false),
                    idStana = table.Column<Guid>(nullable: false),
                    cena = table.Column<int>(nullable: false),
                    naslovOglasa = table.Column<string>(nullable: true),
                    opisOglasa = table.Column<string>(nullable: true),
                    datumIsteka = table.Column<DateTime>(nullable: false),
                    prodajaDaNe = table.Column<int>(nullable: false),
                    brojLajkova = table.Column<int>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Oglas", x => x.idOglasa);
                });

            migrationBuilder.CreateTable(
                name: "Stan",
                columns: table => new
                {
                    idStana = table.Column<Guid>(nullable: false),
                    idVlasnika = table.Column<Guid>(nullable: false),
                    povrsina = table.Column<int>(nullable: false),
                    idGrada = table.Column<int>(nullable: false),
                    adresa = table.Column<string>(nullable: true),
                    brojSoba = table.Column<int>(nullable: false),
                    idTipaStana = table.Column<int>(nullable: false),
                    idStanjaObjekta = table.Column<int>(nullable: false),
                    idNamestenosti = table.Column<int>(nullable: false),
                    idGradnje = table.Column<int>(nullable: false),
                    sprat = table.Column<int>(nullable: false),
                    terasa = table.Column<int>(nullable: false),
                    telefon = table.Column<int>(nullable: false),
                    internet = table.Column<int>(nullable: false),
                    KATV = table.Column<int>(nullable: false),
                    interfon = table.Column<int>(nullable: false),
                    lift = table.Column<int>(nullable: false),
                    klima = table.Column<int>(nullable: false),
                    grejanje = table.Column<int>(nullable: false),
                    parking = table.Column<int>(nullable: false),
                    videoNadzor = table.Column<int>(nullable: false),
                    dodatniOpis = table.Column<string>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Stan", x => x.idStana);
                });

            migrationBuilder.CreateTable(
                name: "StanjeObjekta",
                columns: table => new
                {
                    idStanja = table.Column<int>(nullable: false)
                        .Annotation("Sqlite:Autoincrement", true),
                    stanje = table.Column<string>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_StanjeObjekta", x => x.idStanja);
                });

            migrationBuilder.CreateTable(
                name: "TipStana",
                columns: table => new
                {
                    idTipa = table.Column<int>(nullable: false)
                        .Annotation("Sqlite:Autoincrement", true),
                    tipStana = table.Column<string>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_TipStana", x => x.idTipa);
                });

            migrationBuilder.CreateTable(
                name: "Vlasnik",
                columns: table => new
                {
                    idVlasnika = table.Column<Guid>(nullable: false),
                    username = table.Column<string>(nullable: true),
                    punoIme = table.Column<string>(nullable: true),
                    brojTelefona = table.Column<string>(nullable: true),
                    brojOglasa = table.Column<int>(nullable: false),
                    zbirSvihOcena = table.Column<int>(nullable: false),
                    brojOcena = table.Column<int>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Vlasnik", x => x.idVlasnika);
                });
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Grad");

            migrationBuilder.DropTable(
                name: "Gradnja");

            migrationBuilder.DropTable(
                name: "Login");

            migrationBuilder.DropTable(
                name: "Namestenost");

            migrationBuilder.DropTable(
                name: "Oglas");

            migrationBuilder.DropTable(
                name: "Stan");

            migrationBuilder.DropTable(
                name: "StanjeObjekta");

            migrationBuilder.DropTable(
                name: "TipStana");

            migrationBuilder.DropTable(
                name: "Vlasnik");
        }
    }
}
