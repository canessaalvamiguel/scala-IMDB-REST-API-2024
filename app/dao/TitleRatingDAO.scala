package daos

import models.TitleBasicsTable.titleBasics
import models.{TitleRating, TitleRatingsTable}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class TitleRatingDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val titleRatings = TitleRatingsTable.titleRatings

  def all(): Future[Seq[TitleRating]] = db.run(titleRatings.take(10).result)

  def insert(titleRating: TitleRating): Future[Int] = db.run(titleRatings += titleRating)

  def findById(tconst: String): Future[Option[TitleRating]] = db.run(titleRatings.filter(_.tconst === tconst).result.headOption)

  def update(tconst: String, updatedTitleRating: TitleRating): Future[Int] = db.run(titleRatings.filter(_.tconst === tconst).update(updatedTitleRating))

  def delete(tconst: String): Future[Int] = db.run(titleRatings.filter(_.tconst === tconst).delete)

  def getTopRatedMoviesByGenre(genre: String): Future[Seq[(TitleRating, String)]] = {
    val lowerGenre = genre.toLowerCase
    val query = titleRatings
      .join(titleBasics).on(_.tconst === _.tconst)
      .filter(_._2.genres.toLowerCase.like(s"%$lowerGenre%"))
      .sortBy(s => (s._1.averageRating.desc, s._1.numVotes.desc, s._2.originalTitle.asc))
      .take(10)
      .map { case (rating, basic) => (rating, basic.originalTitle.getOrElse("")) }
    db.run(query.result)
  }
}

