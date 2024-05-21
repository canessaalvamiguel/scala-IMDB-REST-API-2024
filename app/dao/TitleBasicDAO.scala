package daos

import javax.inject.Inject
import models.{TitleBasic, TitleBasicsTable}
import slick.jdbc.JdbcProfile
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.{ExecutionContext, Future}

class TitleBasicDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val titleBasics = TitleBasicsTable.titleBasics

  def all(): Future[Seq[TitleBasic]] = db.run(titleBasics.take(10).result)

  def insert(titleBasic: TitleBasic): Future[Int] = db.run(titleBasics += titleBasic)

  def findById(tconst: String): Future[Option[TitleBasic]] = db.run(titleBasics.filter(_.tconst === tconst).result.headOption)

  def update(tconst: String, updatedTitleBasic: TitleBasic): Future[Int] = db.run(titleBasics.filter(_.tconst === tconst).update(updatedTitleBasic))

  def delete(tconst: String): Future[Int] = db.run(titleBasics.filter(_.tconst === tconst).delete)
}
