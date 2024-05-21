package actors

import actors.TitleRatingActor.{Delete, GetAll, GetById, GetTopRatedMoviesByGenre, Insert, MoviesByOriginalName, Update}
import akka.actor.{Actor, Props}
import daos.TitleRatingDAO
import models.TitleRating

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class TitleRatingActor @Inject()(titleRatingDAO: TitleRatingDAO)(implicit ec: ExecutionContext) extends Actor {

  def receive: Receive = {
    case GetTopRatedMoviesByGenre(genre) =>
      val senderRef = sender()
      titleRatingDAO.getTopRatedMoviesByGenre(genre).map { rated =>
        senderRef ! MoviesByOriginalName(rated)
      }
    case GetAll() =>
      val senderRef = sender()
      titleRatingDAO.all().map { rating =>
        senderRef ! rating
      }
    case Insert(titleBasic) =>
      val senderRef = sender()
      titleRatingDAO.insert(titleBasic).map { result =>
        senderRef ! result
      }
    case GetById(tconst) =>
      val senderRef = sender()
      titleRatingDAO.findById(tconst).map { rating =>
        senderRef ! rating
      }
    case Update(tconst, titleBasic) =>
      val senderRef = sender()
      titleRatingDAO.update(tconst, titleBasic).map { result =>
        senderRef ! result
      }
    case Delete(tconst) =>
      val senderRef = sender()
      titleRatingDAO.delete(tconst).map { result =>
        senderRef ! result
      }
  }
}

object TitleRatingActor {
  case class GetTopRatedMoviesByGenre(originalName: String)
  case class MoviesByOriginalName(movies: Seq[(TitleRating, String)])
  case class GetMovieByTitle(title: String)
  case class GetAll()
  case class Insert(titleBasic: TitleRating)
  case class GetById(tconst: String)
  case class Update(tconst: String, titleBasic: TitleRating)
  case class Delete(tconst: String)

  def props(titleRatingDAO: TitleRatingDAO)(implicit ec: ExecutionContext): Props =
    Props(new TitleRatingActor(titleRatingDAO))
}