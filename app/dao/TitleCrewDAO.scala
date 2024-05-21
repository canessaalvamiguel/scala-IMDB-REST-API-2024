package daos

import javax.inject.Inject
import models.{TitleCrew, TitleCrewsTable}
import slick.jdbc.JdbcProfile
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.{ExecutionContext, Future}

class TitleCrewDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val titleCrews = TitleCrewsTable.titleCrews

  def all(): Future[Seq[TitleCrew]] = db.run(titleCrews.take(10).result)

  def insert(titleCrew: TitleCrew): Future[Int] = db.run(titleCrews += titleCrew)

  def findById(tconst: String): Future[Option[TitleCrew]] = db.run(titleCrews.filter(_.tconst === tconst).result.headOption)

  def update(tconst: String, updatedTitleCrew: TitleCrew): Future[Int] = db.run(titleCrews.filter(_.tconst === tconst).update(updatedTitleCrew))

  def delete(tconst: String): Future[Int] = db.run(titleCrews.filter(_.tconst === tconst).delete)
}
