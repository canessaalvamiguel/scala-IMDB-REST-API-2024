package actors

import actors.TitleRatingActor.{GetTopRatedMoviesByGenre, MoviesByOriginalName}
import akka.actor.{Actor, Props}
import daos.TitleRatingDAO
import models.TitleRating

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class TitleRatingActor @Inject()(titleRatingDAO: TitleRatingDAO)(implicit ec: ExecutionContext) extends Actor {

  def receive: Receive = {
    case GetTopRatedMoviesByGenre(genre) =>
      val senderRef = sender()
      titleRatingDAO.getTopRatedMoviesByGenre(genre).map { movies =>
        senderRef ! MoviesByOriginalName(movies)
      }
  }
}

object TitleRatingActor {
  case class GetTopRatedMoviesByGenre(originalName: String)
  case class MoviesByOriginalName(movies: Seq[(TitleRating, String)])

  def props(titleRatingDAO: TitleRatingDAO)(implicit ec: ExecutionContext): Props =
    Props(new TitleRatingActor(titleRatingDAO))
}