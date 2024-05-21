package daos

import javax.inject.Inject
import models.{TitleRating, TitleRatingsTable}
import slick.jdbc.JdbcProfile
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.{ExecutionContext, Future}

class TitleRatingDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val titleRatings = TitleRatingsTable.titleRatings

  def all(): Future[Seq[TitleRating]] = db.run(titleRatings.take(10).result)

  def insert(titleRating: TitleRating): Future[Int] = db.run(titleRatings += titleRating)

  def findById(tconst: String): Future[Option[TitleRating]] = db.run(titleRatings.filter(_.tconst === tconst).result.headOption)

  def update(tconst: String, updatedTitleRating: TitleRating): Future[Int] = db.run(titleRatings.filter(_.tconst === tconst).update(updatedTitleRating))

  def delete(tconst: String): Future[Int] = db.run(titleRatings.filter(_.tconst === tconst).delete)
}

