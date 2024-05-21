package services

import javax.inject._
import daos.TitlePrincipalDAO
import models.TitlePrincipal

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TitlePrincipalService @Inject()(titlePrincipalDAO: TitlePrincipalDAO)(implicit ec: ExecutionContext) {

  def getAll(): Future[Seq[TitlePrincipal]] = {
    titlePrincipalDAO.all()
  }

  def create(titlePrincipal: TitlePrincipal): Future[Int] = {
    titlePrincipalDAO.insert(titlePrincipal)
  }

  def getById(tconst: String, ordering: Int, nconst: String): Future[Option[TitlePrincipal]] = {
    titlePrincipalDAO.findById(tconst, ordering, nconst)
  }

  def update(tconst: String, ordering: Int, nconst: String, titlePrincipal: TitlePrincipal): Future[Int] = {
    titlePrincipalDAO.update(tconst, ordering, nconst, titlePrincipal)
  }

  def delete(tconst: String, ordering: Int, nconst: String): Future[Int] = {
    titlePrincipalDAO.delete(tconst, ordering, nconst)
  }
}
