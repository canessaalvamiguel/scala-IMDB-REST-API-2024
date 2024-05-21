package models

import slick.jdbc.PostgresProfile.api._

case class TitlePrincipal(
                           tconst: String,
                           ordering: Int,
                           nconst: String,
                           category: Option[String],
                           job: Option[String],
                           characters: Option[String]
                         )

class TitlePrincipalsTable(tag: Tag) extends Table[TitlePrincipal](tag, "title_principals") {
  def tconst = column[String]("tconst")
  def ordering = column[Int]("ordering")
  def nconst = column[String]("nconst")
  def category = column[Option[String]]("category")
  def job = column[Option[String]]("job")
  def characters = column[Option[String]]("characters")

  def pk = primaryKey("pk_title_principals", (tconst, ordering, nconst))

  def * = (tconst, ordering, nconst, category, job, characters) <> ((TitlePrincipal.apply _).tupled, TitlePrincipal.unapply)

  def titleBasic = foreignKey("fk_title_basics", tconst, TableQuery[TitleBasicsTable])(_.tconst)
  def nameBasic = foreignKey("fk_name_basics", nconst, TableQuery[NameBasicsTable])(_.nconst)
}

object TitlePrincipalsTable {
  val titlePrincipals = TableQuery[TitlePrincipalsTable]
}
