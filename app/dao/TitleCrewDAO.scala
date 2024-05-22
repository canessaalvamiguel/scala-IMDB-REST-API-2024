package daos

import javax.inject.Inject
import models.{TitleCrew, TitleCrewsTable}
import slick.jdbc.JdbcProfile
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.exceptions.DatabaseException

import java.sql.SQLException
import scala.concurrent.{ExecutionContext, Future}

class TitleCrewDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val titleCrews = TitleCrewsTable.titleCrews

  def all(): Future[Seq[TitleCrew]] = handleExceptions {
    db.run(titleCrews.take(10).result)
  }

  def insert(titleCrew: TitleCrew): Future[Int] = handleExceptions {
    db.run(titleCrews += titleCrew)
  }

  def findById(tconst: String): Future[Option[TitleCrew]] = handleExceptions {
    db.run(titleCrews.filter(_.tconst === tconst).result.headOption)
  }

  def update(tconst: String, updatedTitleCrew: TitleCrew): Future[Int] = handleExceptions {
    db.run(titleCrews.filter(_.tconst === tconst).update(updatedTitleCrew))
  }

  def delete(tconst: String): Future[Int] = handleExceptions {
    db.run(titleCrews.filter(_.tconst === tconst).delete)
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
