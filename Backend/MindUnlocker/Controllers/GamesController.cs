using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web.Http;
using System.Web.Http.Description;
using Microsoft.AspNet.Identity;
using MindUnlocker.Models;

namespace MindUnlocker.Controllers
{
    [Authorize]
    public class GamesController : ApiController
    {
        private ApplicationDbContext db = new ApplicationDbContext();


        // Returns game history for a given player
        // GET: api/Games
        public List<Game> GetGames()
        {
            string  userID = User.Identity.GetUserId();
            List<Game> games = db.Games.Where(x => x.User.Id == userID).ToList();

            return games;
        }

        //get details for a game
        // GET: api/Games/5
        [ResponseType(typeof(Game))]
        public IHttpActionResult GetGame(int id)
        {
            Game game = db.Games.Find(id);
            if (game == null)
            {
                return NotFound();
            }
            return Ok(game);
        }
    
        // insert a new game into it's account 
        // 
        // POST: api/Games
        [ResponseType(typeof(Game))]
        public IHttpActionResult PostGame(GameDto game)
        {
            string UserId = User.Identity.GetUserId();

            if (game != null)
            {
                Dificulty df = Dificulty.Easy;
                if (game.Dificulty.Equals("Easy"))
                {
                    df = Dificulty.Easy;
                }else if (game.Dificulty.Equals("Medium"))
                {
                    df = Dificulty.Medium;
                }
                else if (game.Dificulty.Equals("Hard"))
                {
                    df = Dificulty.Hard;
                }
                Game g = new Game
                {
                    Dificulty = df,
                    Hints = game.Hints,
                    Points= game.Points,
                    Duration= game.Duration
                };
                g.User = db.Users.Where(x => x.Id == UserId).FirstOrDefault();
                db.Games.Add(g);


                db.SaveChanges();

                return CreatedAtRoute("DefaultApi", new { id = g.ID }, game);

            }
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            return Unauthorized();

        }


        /*
         * 
         *     // PUT: api/Games/5
        [ResponseType(typeof(void))]
        public IHttpActionResult PutGame(int id, Game game)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != game.ID)
            {
                return BadRequest();
            }

            db.Entry(game).State = EntityState.Modified;

            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!GameExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }


        // DELETE: api/Games/5
        [ResponseType(typeof(Game))]
        public IHttpActionResult DeleteGame(int id)
        {
            Game game = db.Games.Find(id);
            if (game == null)
            {
                return NotFound();
            }

            db.Games.Remove(game);
            db.SaveChanges();

            return Ok(game);
        }
        */

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