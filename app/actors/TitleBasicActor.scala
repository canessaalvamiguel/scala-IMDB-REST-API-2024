package actors

import actors.TitleBasicActor.{Delete, GetAll, GetById, GetMovieByTitle, Insert, MovieInfo, Update}
import akka.actor.{Actor, Props}
import daos.TitleBasicDAO
import models.TitleBasic
import services.dto.MovieWithDetailsDTO

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class TitleBasicActor @Inject()(titleBasicDAO: TitleBasicDAO)(implicit ec: ExecutionContext) extends Actor {

  def receive: Receive = {
    case GetMovieByTitle(title) =>
      val senderRef = sender()
      titleBasicDAO.searchByTitle(title).map { movies =>
        senderRef ! MovieInfo(movies)
      }
    case GetAll() =>
      val senderRef = sender()
      titleBasicDAO.all().map { movies =>
        senderRef ! movies
      }
    case Insert(titleBasic) =>
      val senderRef = sender()
      titleBasicDAO.insert(titleBasic).map { result =>
        senderRef ! result
      }
    case GetById(tconst) =>
      val senderRef = sender()
      titleBasicDAO.findById(tconst).map { movie =>
        senderRef ! movie
      }
    case Update(tconst, titleBasic) =>
      val senderRef = sender()
      titleBasicDAO.update(tconst, titleBasic).map { result =>
        senderRef ! result
      }
    case Delete(tconst) =>
      val senderRef = sender()
      titleBasicDAO.delete(tconst).map { result =>
        senderRef ! result
      }
  }
}

object TitleBasicActor {
  case class GetMovieByTitle(title: String)
  case class MovieInfo(movie: Seq[MovieWithDetailsDTO])
  case class GetAll()
  case class Insert(titleBasic: TitleBasic)
  case class GetById(tconst: String)
  case class Update(tconst: String, titleBasic: TitleBasic)
  case class Delete(tconst: String)

  def props(titleBasicDAO: TitleBasicDAO)(implicit ec: ExecutionContext): Props =
    Props(new TitleBasicActor(titleBasicDAO))
}