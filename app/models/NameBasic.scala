package models

import slick.jdbc.PostgresProfile.api._

case class NameBasic(
                      nconst: String,
                      primaryName: Option[String],
                      birthYear: Option[Int],
                      deathYear: Option[Int],
                      primaryProfession: Option[String],
                      knownForTitles: Option[String]
                    )

class NameBasicsTable(tag: Tag) extends Table[NameBasic](tag, "name_basics") {
  def nconst = column[String]("nconst", O.PrimaryKey)
  def primaryName = column[Option[String]]("primaryname")
  def birthYear = column[Option[Int]]("birthyear")
  def deathYear = column[Option[Int]]("deathyear")
  def primaryProfession = column[Option[String]]("primaryprofession")
  def knownForTitles = column[Option[String]]("knownfortitles")

  def * = (nconst, primaryName, birthYear, deathYear, primaryProfession, knownForTitles) <> ((NameBasic.apply _).tupled, NameBasic.unapply)
}

object NameBasicsTable {
  val nameBasics = TableQuery[NameBasicsTable]
}
