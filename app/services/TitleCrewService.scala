package services

import javax.inject._
import daos.TitleCrewDAO
import models.TitleCrew

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TitleCrewService @Inject()(titleCrewDAO: TitleCrewDAO)(implicit ec: ExecutionContext) {

  def getAll(): Future[Seq[TitleCrew]] = {
    titleCrewDAO.all()
  }

  def create(titleCrew: TitleCrew): Future[Int] = {
    titleCrewDAO.insert(titleCrew)
  }

  def getById(tconst: String): Future[Option[TitleCrew]] = {
    titleCrewDAO.findById(tconst)
  }

  def update(tconst: String, titleCrew: TitleCrew): Future[Int] = {
    titleCrewDAO.update(tconst, titleCrew)
  }

  def delete(tconst: String): Future[Int] = {
    titleCrewDAO.delete(tconst)
  }
}
