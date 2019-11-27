using MindUnlocker.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace MindUnlocker.Controllers
{
    public class HomeController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        public ActionResult Index()
        {
            List<ApplicationUser> users = db.Users.ToList();

            ViewBag.users = users;
            ViewBag.Title = "Home Page";

            return View();
        }
    }
}
