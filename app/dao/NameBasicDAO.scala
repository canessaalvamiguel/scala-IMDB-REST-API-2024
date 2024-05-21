package daos

import javax.inject.Inject
import models.{NameBasic, NameBasicsTable}
import slick.jdbc.JdbcProfile
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.{ExecutionContext, Future}

class NameBasicDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val nameBasics = NameBasicsTable.nameBasics

  def all(): Future[Seq[NameBasic]] = db.run(nameBasics.take(10).result)

  def insert(nameBasic: NameBasic): Future[Int] = db.run(nameBasics += nameBasic)

  def findById(nconst: String): Future[Option[NameBasic]] = db.run(nameBasics.filter(_.nconst === nconst).result.headOption)

  def update(nconst: String, updatedNameBasic: NameBasic): Future[Int] = db.run(nameBasics.filter(_.nconst === nconst).update(updatedNameBasic))

  def delete(nconst: String): Future[Int] = db.run(nameBasics.filter(_.nconst === nconst).delete)
}
