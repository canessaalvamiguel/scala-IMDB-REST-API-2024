package daos

import models.{NameBasic, NameBasicsTable}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.exceptions.DatabaseException
import slick.jdbc.JdbcProfile

import java.sql.SQLException
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class NameBasicDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val nameBasics = NameBasicsTable.nameBasics

  def all(): Future[Seq[NameBasic]] = handleExceptions {
    db.run(nameBasics.take(10).result)
  }

  def insert(nameBasic: NameBasic): Future[Int] = handleExceptions {
    db.run(nameBasics += nameBasic)
  }

  def findById(nconst: String): Future[Option[NameBasic]] = handleExceptions {
    db.run(nameBasics.filter(_.nconst === nconst).result.headOption)
  }

  def update(nconst: String, updatedNameBasic: NameBasic): Future[Int] = handleExceptions {
    db.run(nameBasics.filter(_.nconst === nconst).update(updatedNameBasic))
  }

  def delete(nconst: String): Future[Int] = handleExceptions {
    db.run(nameBasics.filter(_.nconst === nconst).delete)
  }

  private def handleExceptions[T](operation: => Future[T]): Future[T] = {
    operation.recover {
      case e: SQLException =>
        throw DatabaseException("Database operation failed due to SQL exception", e)
      case e: Exception =>
        throw DatabaseException("Database operation failed", e)
    }
  }
}
