package services

import javax.inject._
import daos.TitleBasicDAO
import models.TitleBasic

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TitleBasicService @Inject()(titleBasicDAO: TitleBasicDAO)(implicit ec: ExecutionContext) {

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
}
