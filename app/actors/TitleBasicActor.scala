package actors

import actors.TitleBasicActor._
import akka.actor.{Actor, Props}
import akka.pattern.pipe
import daos.TitleBasicDAO
import models.TitleBasic
import services.dto.MovieWithDetailsDTO

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class TitleBasicActor @Inject()(titleBasicDAO: TitleBasicDAO)(implicit ec: ExecutionContext) extends Actor {

  def receive: Receive = {
    case GetMovieByTitle(title) =>
      titleBasicDAO.searchByTitle(title) pipeTo sender()

    case GetAll() =>
      titleBasicDAO.all() pipeTo sender()

    case Insert(titleBasic) =>
      titleBasicDAO.insert(titleBasic) pipeTo sender()

    case GetById(tconst) =>
      titleBasicDAO.findById(tconst) pipeTo sender()

    case Update(tconst, titleBasic) =>
      titleBasicDAO.update(tconst, titleBasic) pipeTo sender()

    case Delete(tconst) =>
      titleBasicDAO.delete(tconst) pipeTo sender()
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