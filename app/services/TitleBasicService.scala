package services

import actors.TitleBasicActor
import actors.TitleBasicActor._
import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import daos.TitleBasicDAO
import models.TitleBasic
import services.dto.MovieWithDetailsDTO

import javax.inject._
import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TitleBasicService @Inject()(titleBasicDAO: TitleBasicDAO)(implicit ec: ExecutionContext) {

  val actorSystem = ActorSystem("MovieActorSystem")
  private val titleBasicActor: ActorRef = actorSystem.actorOf(TitleBasicActor.props(titleBasicDAO), "titleBasicActor")
  implicit val timeout: Timeout = 5.seconds

  def getAll(): Future[Seq[TitleBasic]] = {
    (titleBasicActor ? GetAll()).mapTo[Seq[TitleBasic]]
  }

  def create(titleBasic: TitleBasic): Future[Int] = {
    (titleBasicActor ? Insert(titleBasic)).mapTo[Int]
  }

  def getById(tconst: String): Future[Option[TitleBasic]] = {
    (titleBasicActor ? GetById(tconst)).mapTo[Option[TitleBasic]]
  }

  def update(tconst: String, titleBasic: TitleBasic): Future[Int] = {
    (titleBasicActor ? Update(tconst, titleBasic)).mapTo[Int]
  }

  def delete(tconst: String): Future[Int] = {
    (titleBasicActor ? Delete(tconst)).mapTo[Int]
  }

  def searchByTitle(title: String): Future[Seq[MovieWithDetailsDTO]] = {
    (titleBasicActor ? GetMovieByTitle(title)).mapTo[Seq[MovieWithDetailsDTO]]
  }
}
