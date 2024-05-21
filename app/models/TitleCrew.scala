package models

import slick.jdbc.PostgresProfile.api._

case class TitleCrew(
                      tconst: String,
                      directors: Option[String],
                      writers: Option[String]
                    )

class TitleCrewsTable(tag: Tag) extends Table[TitleCrew](tag, "title_crew") {
  def tconst = column[String]("tconst", O.PrimaryKey)
  def directors = column[Option[String]]("directors")
  def writers = column[Option[String]]("writers")

  def * = (tconst, directors, writers) <> ((TitleCrew.apply _).tupled, TitleCrew.unapply)

  def titleBasic = foreignKey("fk_title_basics", tconst, TableQuery[TitleBasicsTable])(_.tconst)
}

object TitleCrewsTable {
  val titleCrews = TableQuery[TitleCrewsTable]
}
