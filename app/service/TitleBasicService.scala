package services

import actors.TitleBasicActor
import actors.TitleBasicActor.{GetMovieByTitle, MovieInfo}
import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import daos.TitleBasicDAO
import models.TitleBasic
import akka.pattern.ask
import service.dto.MovieWithDetailsDTO

import javax.inject._
import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TitleBasicService @Inject()(titleBasicDAO: TitleBasicDAO)(implicit ec: ExecutionContext) {

  val actorSystem = ActorSystem("MovieActorSystem")
  private val titleBasicActor: ActorRef = actorSystem.actorOf(TitleBasicActor.props(titleBasicDAO), "titleBasicActor")
  implicit val timeout: Timeout = 5.seconds

  def getAll(): Future[Seq[TitleBasic]] = {
    titleBasicDAO.all()
  }

  def create(titleBasic: TitleBasic): Future[Int] = {
    titleBasicDAO.insert(titleBasic)
  }

  def getById(tconst: String): Future[Option[TitleBasic]] = {
    titleBasicDAO.findById(tconst)
  }

  def update(tconst: String, titleBasic: TitleBasic): Future[Int] = {
    titleBasicDAO.update(tconst, titleBasic)
  }

  def delete(tconst: String): Future[Int] = {
    titleBasicDAO.delete(tconst)
  }

  def searchByTitle(title: String): Future[Seq[MovieWithDetailsDTO]] = {
    (titleBasicActor ? GetMovieByTitle(title)).map {
      case MovieInfo(movies) => movies
    }
  }
}
