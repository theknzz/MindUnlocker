namespace MindUnlocker.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class gameschanged : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Games", "Duration", c => c.Int(nullable: false));
        }
        
        public override void Down()
        {
            DropColumn("dbo.Games", "Duration");
        }
    }
}
