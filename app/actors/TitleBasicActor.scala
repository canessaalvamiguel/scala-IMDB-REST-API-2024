package actors

import actors.TitleBasicActor.{GetTopRatedMoviesByGenre, MoviesByOriginalName}
import akka.actor.{Actor, Props}
import models.TitleBasicsTable.titleBasics
import models.TitleRating
import models.TitleRatingsTable.titleRatings
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.PostgresProfile.api._

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class TitleBasicActor @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends Actor {

  def receive: Receive = {
    case GetTopRatedMoviesByGenre(genre) =>
      val senderRef = sender()
      val lowerGenre = genre.toLowerCase
      val query = titleRatings
        .join(titleBasics).on(_.tconst === _.tconst)
        .filter(_._2.genres.toLowerCase.like(s"%$lowerGenre%"))
        .sortBy(s => (s._1.averageRating.desc, s._1.numVotes.desc, s._2.originalTitle.asc))
        .take(10)
        .map { case (rating, basic) => (rating, basic.originalTitle.getOrElse("")) }
      dbConfigProvider.get.db.run(query.result).map { movies =>
        senderRef ! MoviesByOriginalName(movies)
      }
  }
}

object TitleBasicActor {
  case class GetTopRatedMoviesByGenre(originalName: String)
  case class MoviesByOriginalName(movies: Seq[(TitleRating, String)])

  def props(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext): Props =
    Props(new TitleBasicActor(dbConfigProvider))
}