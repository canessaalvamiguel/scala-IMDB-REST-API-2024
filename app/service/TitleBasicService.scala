package services

import daos.{NameBasicDAO, TitleBasicDAO}
import models.TitleBasic
import service.MovieWithDetailsDTO

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TitleBasicService @Inject()(titleBasicDAO: TitleBasicDAO, nameBasicDAO: NameBasicDAO)(implicit ec: ExecutionContext) {

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
    titleBasicDAO.searchByTitle(title)
  }
}
