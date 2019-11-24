using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace MindUnlocker.Models
{
    public class GamesPlayed
    {
        public int Easy { get; set;}
        public int Medium { get; set;}
        public int Hard { get; set; }
    }
    public class TopUser
    {
        public string Name { get; set; }
        public int Points { get; set; }
        public int TotalGames { get; set; }
        public GamesPlayed GamesPlayed { get; set; }
        public int Hints { get; set; }

    }
}