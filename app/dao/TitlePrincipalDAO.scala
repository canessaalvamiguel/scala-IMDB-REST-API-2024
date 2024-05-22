package daos

import javax.inject.Inject
import models.{TitlePrincipal, TitlePrincipalsTable}
import slick.jdbc.JdbcProfile
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.exceptions.DatabaseException

import java.sql.SQLException
import scala.concurrent.{ExecutionContext, Future}

class TitlePrincipalDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val titlePrincipals = TitlePrincipalsTable.titlePrincipals

  def all(): Future[Seq[TitlePrincipal]] = handleExceptions {
    db.run(titlePrincipals.take(10).result)
  }

  def insert(titlePrincipal: TitlePrincipal): Future[Int] = handleExceptions {
    db.run(titlePrincipals += titlePrincipal)
  }

  def findById(tconst: String, ordering: Int, nconst: String): Future[Option[TitlePrincipal]] = handleExceptions {
    db.run(titlePrincipals.filter(tp => tp.tconst === tconst && tp.ordering === ordering && tp.nconst === nconst).result.headOption)
  }

  def update(tconst: String, ordering: Int, nconst: String, updatedTitlePrincipal: TitlePrincipal): Future[Int] = handleExceptions {
    db.run(titlePrincipals.filter(tp => tp.tconst === tconst && tp.ordering === ordering && tp.nconst === nconst).update(updatedTitlePrincipal))
  }

  def delete(tconst: String, ordering: Int, nconst: String): Future[Int] = handleExceptions {
    db.run(titlePrincipals.filter(tp => tp.tconst === tconst && tp.ordering === ordering && tp.nconst === nconst).delete)
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
