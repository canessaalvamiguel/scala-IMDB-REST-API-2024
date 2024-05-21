package models

import slick.jdbc.PostgresProfile.api._

case class TitleRating(
                        tconst: String,
                        averageRating: Double,
                        numVotes: Int
                      )

class TitleRatingsTable(tag: Tag) extends Table[TitleRating](tag, "title_ratings") {
  def tconst = column[String]("tconst", O.PrimaryKey)
  def averageRating = column[Double]("averagerating")
  def numVotes = column[Int]("numvotes")

  def * = (tconst, averageRating, numVotes) <> ((TitleRating.apply _).tupled, TitleRating.unapply)

  def titleBasic = foreignKey("fk_title_basics", tconst, TableQuery[TitleBasicsTable])(_.tconst)
}

object TitleRatingsTable {
  val titleRatings = TableQuery[TitleRatingsTable]
}
