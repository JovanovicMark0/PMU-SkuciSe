using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace PlatinumBCKND.Migrations
{
    public partial class third : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "OmiljeniOglas",
                columns: table => new
                {
                    idVlasnika = table.Column<Guid>(nullable: false),
                    idOglasa = table.Column<Guid>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_OmiljeniOglas", x => x.idVlasnika);
                });
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "OmiljeniOglas");
        }
    }
}
