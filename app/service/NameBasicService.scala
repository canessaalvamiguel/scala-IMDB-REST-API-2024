package services

import javax.inject._
import daos.NameBasicDAO
import models.NameBasic

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class NameBasicService @Inject()(nameBasicDAO: NameBasicDAO)(implicit ec: ExecutionContext) {

  def getAll(): Future[Seq[NameBasic]] = {
    nameBasicDAO.all()
  }

  def create(nameBasic: NameBasic): Future[Int] = {
    nameBasicDAO.insert(nameBasic)
  }

  def getById(nconst: String): Future[Option[NameBasic]] = {
    nameBasicDAO.findById(nconst)
  }

  def update(nconst: String, nameBasic: NameBasic): Future[Int] = {
    nameBasicDAO.update(nconst, nameBasic)
  }

  def delete(nconst: String): Future[Int] = {
    nameBasicDAO.delete(nconst)
  }
}

