package actors

import actors.TitleRatingActor._
import akka.actor.{Actor, Props}
import akka.pattern.pipe
import daos.TitleRatingDAO
import models.TitleRating

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class TitleRatingActor @Inject()(titleRatingDAO: TitleRatingDAO)(implicit ec: ExecutionContext) extends Actor {

  def receive: Receive = {
    case GetTopRatedMoviesByGenre(genre) =>
      titleRatingDAO.getTopRatedMoviesByGenre(genre) pipeTo sender()

    case GetAll() =>
      titleRatingDAO.all() pipeTo sender()

    case Insert(titleBasic) =>
      titleRatingDAO.insert(titleBasic) pipeTo sender()

    case GetById(tconst) =>
      titleRatingDAO.findById(tconst) pipeTo sender()

    case Update(tconst, titleBasic) =>
      titleRatingDAO.update(tconst, titleBasic) pipeTo sender()

    case Delete(tconst) =>
      titleRatingDAO.delete(tconst) pipeTo sender()
  }
}

object TitleRatingActor {
  case class GetTopRatedMoviesByGenre(originalName: String)
  case class GetMovieByTitle(title: String)
  case class GetAll()
  case class Insert(titleBasic: TitleRating)
  case class GetById(tconst: String)
  case class Update(tconst: String, titleBasic: TitleRating)
  case class Delete(tconst: String)

  def props(titleRatingDAO: TitleRatingDAO)(implicit ec: ExecutionContext): Props =
    Props(new TitleRatingActor(titleRatingDAO))
}