using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace MindUnlocker.Models
{
    public class GameDto
    {
        public int Points { get; set; }
        public int Duration { get; set; }  //Seconds
        public String Dificulty { get; set; }  //Easy, Medium, Hard
        public int Hints { get; set; }
    }
}
 