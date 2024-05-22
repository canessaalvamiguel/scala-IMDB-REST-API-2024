package daos

import models.TitleBasicsTable.titleBasics
import models.{TitleRating, TitleRatingsTable}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.exceptions.DatabaseException
import slick.jdbc.JdbcProfile

import java.sql.SQLException
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class TitleRatingDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val titleRatings = TitleRatingsTable.titleRatings

  def all(): Future[Seq[TitleRating]] = handleExceptions {
    db.run(titleRatings.take(10).result)
  }

  def insert(titleRating: TitleRating): Future[Int] = handleExceptions {
    db.run(titleRatings += titleRating)
  }

  def findById(tconst: String): Future[Option[TitleRating]] = handleExceptions {
    db.run(titleRatings.filter(_.tconst === tconst).result.headOption)
  }

  def update(tconst: String, updatedTitleRating: TitleRating): Future[Int] = handleExceptions {
    db.run(titleRatings.filter(_.tconst === tconst).update(updatedTitleRating))
  }

  def delete(tconst: String): Future[Int] = handleExceptions {
    db.run(titleRatings.filter(_.tconst === tconst).delete)
  }

  def getTopRatedMoviesByGenre(genre: String): Future[Seq[(TitleRating, String)]] = handleExceptions {
    val lowerGenre = genre.toLowerCase
    val query = titleRatings
      .join(titleBasics).on(_.tconst === _.tconst)
      .filter(_._2.genres.toLowerCase.like(s"%$lowerGenre%"))
      .sortBy(s => (s._1.averageRating.desc, s._1.numVotes.desc, s._2.originalTitle.asc))
      .take(10)
      .map { case (rating, basic) => (rating, basic.originalTitle.getOrElse("")) }
    db.run(query.result)
  }

  private def handleExceptions[T](operation: => Future[T]): Future[T] = {
    operation.recover {
      case e: SQLException =>
        throw DatabaseException("Database operation failed due to SQL exception", e)
      case e: Exception =>
        throw DatabaseException("Database operation failed", e)
    }
  }
}

