using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace MindUnlocker.Models
{
    public enum Dificulty
    {
        Easy,
        Medium,
        Hard
    }

    public class Game
    {
        public int ID { get; set; }
        public ApplicationUser User { get; set; }
        public int Points { get; set; }
        public Dificulty Dificulty { get; set; }
        public int Hints { get; set; }           
        public int Duration { get; set; }
    }
}