package services

import javax.inject._
import daos.TitleRatingDAO
import models.TitleRating

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TitleRatingService @Inject()(titleRatingDAO: TitleRatingDAO)(implicit ec: ExecutionContext) {

  def getAll(): Future[Seq[TitleRating]] = {
    titleRatingDAO.all()
  }

  def create(titleRating: TitleRating): Future[Int] = {
    titleRatingDAO.insert(titleRating)
  }

  def getById(tconst: String): Future[Option[TitleRating]] = {
    titleRatingDAO.findById(tconst)
  }

  def update(tconst: String, titleRating: TitleRating): Future[Int] = {
    titleRatingDAO.update(tconst, titleRating)
  }

  def delete(tconst: String): Future[Int] = {
    titleRatingDAO.delete(tconst)
  }
}

