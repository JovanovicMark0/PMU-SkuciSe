using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace PlatinumBCKND.Migrations
{
    public partial class peta : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropPrimaryKey(
                name: "PK_OmiljeniOglas",
                table: "OmiljeniOglas");

            migrationBuilder.DropColumn(
                name: "dodatniOpis",
                table: "Stan");

            migrationBuilder.DropColumn(
                name: "idGrada",
                table: "Stan");

            migrationBuilder.AddColumn<string>(
                name: "email",
                table: "Vlasnik",
                nullable: true);

            migrationBuilder.AddColumn<string>(
                name: "grad",
                table: "Stan",
                nullable: true);

            migrationBuilder.AddColumn<string>(
                name: "email",
                table: "Login",
                nullable: true);

            migrationBuilder.AddColumn<byte[]>(
                name: "slika",
                table: "Image",
                nullable: true);

            migrationBuilder.AddPrimaryKey(
                name: "PK_OmiljeniOglas",
                table: "OmiljeniOglas",
                column: "idOglasa");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropPrimaryKey(
                name: "PK_OmiljeniOglas",
                table: "OmiljeniOglas");

            migrationBuilder.DropColumn(
                name: "email",
                table: "Vlasnik");

            migrationBuilder.DropColumn(
                name: "grad",
                table: "Stan");

            migrationBuilder.DropColumn(
                name: "email",
                table: "Login");

            migrationBuilder.DropColumn(
                name: "slika",
                table: "Image");

            migrationBuilder.AddColumn<string>(
                name: "dodatniOpis",
                table: "Stan",
                type: "TEXT",
                nullable: true);

            migrationBuilder.AddColumn<int>(
                name: "idGrada",
                table: "Stan",
                type: "INTEGER",
                nullable: false,
                defaultValue: 0);

            migrationBuilder.AddPrimaryKey(
                name: "PK_OmiljeniOglas",
                table: "OmiljeniOglas",
                column: "idVlasnika");
        }
    }
}
