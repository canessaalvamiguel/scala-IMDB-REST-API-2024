package models

import slick.jdbc.PostgresProfile.api._

case class TitleBasic(
                       tconst: String,
                       titleType: Option[String],
                       primaryTitle: Option[String],
                       originalTitle: Option[String],
                       isAdult: Option[Boolean],
                       startYear: Option[Int],
                       endYear: Option[Int],
                       runtimeMinutes: Option[Int],
                       genres: Option[String]
                     )

class TitleBasicsTable(tag: Tag) extends Table[TitleBasic](tag, "title_basics") {
  def tconst = column[String]("tconst", O.PrimaryKey)
  def titleType = column[Option[String]]("titletype")
  def primaryTitle = column[Option[String]]("primarytitle")
  def originalTitle = column[Option[String]]("originaltitle")
  def isAdult = column[Option[Boolean]]("isadult")
  def startYear = column[Option[Int]]("startyear")
  def endYear = column[Option[Int]]("endyear")
  def runtimeMinutes = column[Option[Int]]("runtimeminutes")
  def genres = column[Option[String]]("genres")

  def * = (tconst, titleType, primaryTitle, originalTitle, isAdult, startYear, endYear, runtimeMinutes, genres) <> ((TitleBasic.apply _).tupled, TitleBasic.unapply)
}

object TitleBasicsTable {
  val titleBasics = TableQuery[TitleBasicsTable]
}