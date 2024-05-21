package daos

import actors.TitleBasicActor
import actors.TitleBasicActor.{GetTopRatedMoviesByGenre, MoviesByOriginalName}
import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import models.{TitleRating, TitleRatingsTable}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class TitleRatingDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val titleRatings = TitleRatingsTable.titleRatings
  val actorSystem = ActorSystem("MovieActorSystem")
  private val titleBasicActor: ActorRef = actorSystem.actorOf(TitleBasicActor.props(dbConfigProvider), "titleBasicActor")
  implicit val timeout: Timeout = 5.seconds

  def all(): Future[Seq[TitleRating]] = db.run(titleRatings.take(10).result)

  def insert(titleRating: TitleRating): Future[Int] = db.run(titleRatings += titleRating)

  def findById(tconst: String): Future[Option[TitleRating]] = db.run(titleRatings.filter(_.tconst === tconst).result.headOption)

  def update(tconst: String, updatedTitleRating: TitleRating): Future[Int] = db.run(titleRatings.filter(_.tconst === tconst).update(updatedTitleRating))

  def delete(tconst: String): Future[Int] = db.run(titleRatings.filter(_.tconst === tconst).delete)

  def getTopRatedMoviesByGenre(genre: String): Future[Seq[(TitleRating, String)]] = {
      (titleBasicActor ? GetTopRatedMoviesByGenre(genre)).map {
        case MoviesByOriginalName(movies) => movies
      }
  }
}

