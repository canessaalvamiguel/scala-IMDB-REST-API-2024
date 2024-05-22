package services

import actors.TitleRatingActor
import actors.TitleRatingActor._
import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import daos.TitleRatingDAO
import models.TitleRating
import services.dto.MovieRatingDTO

import javax.inject._
import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TitleRatingService @Inject()(titleRatingDAO: TitleRatingDAO)(implicit ec: ExecutionContext) {

  val actorSystem = ActorSystem("MovieActorSystem")
  private val titleRatingActor: ActorRef = actorSystem.actorOf(TitleRatingActor.props(titleRatingDAO), "titleRatingActor")
  implicit val timeout: Timeout = 5.seconds

  def getAll(): Future[Seq[TitleRating]] = {
    (titleRatingActor ? GetAll()).mapTo[Seq[TitleRating]]
  }

  def create(titleRating: TitleRating): Future[Int] = {
    (titleRatingActor ? Insert(titleRating)).mapTo[Int]
  }

  def getById(tconst: String): Future[Option[TitleRating]] = {
    (titleRatingActor ? GetById(tconst)).mapTo[Option[TitleRating]]
  }

  def update(tconst: String, titleRating: TitleRating): Future[Int] = {
    (titleRatingActor ? Update(tconst, titleRating)).mapTo[Int]
  }

  def delete(tconst: String): Future[Int] = {
    (titleRatingActor ? Delete(tconst)).mapTo[Int]
  }

  def getTopRatedMoviesByGenre(genre: String): Future[Seq[MovieRatingDTO]] = {
    (titleRatingActor ? GetTopRatedMoviesByGenre(genre)).mapTo[Seq[(TitleRating, String)]].map { movies =>
      movies.map { case (rating, title) =>
        MovieRatingDTO(title, rating.averageRating, rating.numVotes)
      }
    }
  }
}

