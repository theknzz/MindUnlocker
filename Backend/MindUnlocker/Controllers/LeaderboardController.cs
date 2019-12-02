using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;
using MindUnlocker.Models;

namespace MindUnlocker.Controllers
{
    [Authorize]

    public class LeaderboardController : ApiController
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        [HttpGet]
        public List<TopUser> Index(){

            List<TopUser> topUsers = new List<TopUser>();

            List<ApplicationUser> users = db.Users.ToList();

            foreach( var user in users)
            {
                var gamesPlayed = db.Games.Where(x => x.User.Id == user.Id).ToList();

                var countEasy = gamesPlayed.Where(x => x.Dificulty == Dificulty.Easy).Count();
                var countMedium = gamesPlayed.Where(x => x.Dificulty == Dificulty.Medium).Count();
                var countHard = gamesPlayed.Where(x => x.Dificulty == Dificulty.Hard).Count();

                var totalHints = gamesPlayed.Sum(x => x.Hints);
                var totalGames = gamesPlayed.Count();

                //total points:
                var points = 0;
                //máximo pontos num jogo? 
                if (gamesPlayed.Count > 0)
                {
                    points = gamesPlayed.Max(x => x.Points);

                }

                TopUser top = new TopUser
                {
                    GamesPlayed = new GamesPlayed
                    {
                        Easy = countEasy,
                        Medium = countMedium,
                        Hard = countHard
                    },
                    Hints = totalHints,
                    TotalGames = totalGames,
                    Name = user.Name,

                    Points = points
                };

                topUsers.Add(top);
            }

            return topUsers.OrderByDescending(x=>x.Points).Take(10).ToList();
        } 
        
        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool GameExists(int id)
        {
            return db.Games.Count(e => e.ID == id) > 0;
        }
    }
}