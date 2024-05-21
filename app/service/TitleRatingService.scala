package services

import actors.TitleRatingActor
import actors.TitleRatingActor.{GetTopRatedMoviesByGenre, MoviesByOriginalName}
import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import javax.inject._
import daos.TitleRatingDAO
import models.TitleRating
import service.MovieRatingDTO
import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TitleRatingService @Inject()(titleRatingDAO: TitleRatingDAO)(implicit ec: ExecutionContext) {

  val actorSystem = ActorSystem("MovieActorSystem")
  private val titleRatingActor: ActorRef = actorSystem.actorOf(TitleRatingActor.props(titleRatingDAO), "titleRatingActor")
  implicit val timeout: Timeout = 5.seconds

  def getAll(): Future[Seq[TitleRating]] = {
    titleRatingDAO.all()
  }

  def create(titleRating: TitleRating): Future[Int] = {
    titleRatingDAO.insert(titleRating)
  }

  def getById(tconst: String): Future[Option[TitleRating]] = {
    titleRatingDAO.findById(tconst)
  }

  def update(tconst: String, titleRating: TitleRating): Future[Int] = {
    titleRatingDAO.update(tconst, titleRating)
  }

  def delete(tconst: String): Future[Int] = {
    titleRatingDAO.delete(tconst)
  }

  def getTopRatedMoviesByGenre(genre: String): Future[Seq[MovieRatingDTO]] = {
    (titleRatingActor ? GetTopRatedMoviesByGenre(genre)).map {
      case MoviesByOriginalName(movies) => movies.map{
        case (rating, titleType) => MovieRatingDTO(titleType, rating.averageRating, rating.numVotes)
      }
    }
  }
}

