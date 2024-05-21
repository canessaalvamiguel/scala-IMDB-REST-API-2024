package actors

import actors.TitleBasicActor.{GetMovieByTitle, MovieInfo}
import akka.actor.{Actor, Props}
import daos.TitleBasicDAO
import service.MovieWithDetailsDTO

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class TitleBasicActor @Inject()(titleBasicDAO: TitleBasicDAO)(implicit ec: ExecutionContext) extends Actor {

  def receive: Receive = {
    case GetMovieByTitle(title) =>
      val senderRef = sender()
      titleBasicDAO.searchByTitle(title).map { movies =>
        senderRef ! MovieInfo(movies)
      }
  }
}

object TitleBasicActor {
  case class GetMovieByTitle(title: String)
  case class MovieInfo(movie: Seq[MovieWithDetailsDTO])

  def props(titleBasicDAO: TitleBasicDAO)(implicit ec: ExecutionContext): Props =
    Props(new TitleBasicActor(titleBasicDAO))
}