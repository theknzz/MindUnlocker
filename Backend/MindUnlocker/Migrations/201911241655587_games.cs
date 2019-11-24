namespace MindUnlocker.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class games : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Games", "points", c => c.Int(nullable: false));
            AddColumn("dbo.Games", "hints", c => c.Int(nullable: false));
            DropColumn("dbo.Games", "score");
        }
        
        public override void Down()
        {
            AddColumn("dbo.Games", "score", c => c.Int(nullable: false));
            DropColumn("dbo.Games", "hints");
            DropColumn("dbo.Games", "points");
        }
    }
}
