package daos

import models._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import services.dto.{CrewDTO, MovieWithDetailsDTO, PrincipalDTO}
import services.exceptions.DatabaseException
import slick.jdbc.JdbcProfile

import java.sql.SQLException
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class TitleBasicDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val titleBasics = TitleBasicsTable.titleBasics
  private val titlePrincipals = TitlePrincipalsTable.titlePrincipals
  private val titleCrews = TitleCrewsTable.titleCrews
  private val nameBasics = NameBasicsTable.nameBasics

  def all(): Future[Seq[TitleBasic]] = handleExceptions {
    db.run(titleBasics.take(10).result)
  }

  def insert(titleBasic: TitleBasic): Future[Int] = handleExceptions {
    db.run(titleBasics += titleBasic)
  }

  def findById(tconst: String): Future[Option[TitleBasic]] = handleExceptions {
    db.run(titleBasics.filter(_.tconst === tconst).result.headOption)
  }

  def update(tconst: String, updatedTitleBasic: TitleBasic): Future[Int] = handleExceptions {
    db.run(titleBasics.filter(_.tconst === tconst).update(updatedTitleBasic))
  }

  def delete(tconst: String): Future[Int] = handleExceptions {
    db.run(titleBasics.filter(_.tconst === tconst).delete)
  }

  def searchByTitle(title: String): Future[Seq[MovieWithDetailsDTO]] = handleExceptions {
    val query = for {
      basic <- titleBasics if basic.primaryTitle.toLowerCase.like(s"%${title.toLowerCase}%") || basic.originalTitle.toLowerCase.like(s"%${title.toLowerCase}%")
      principals <- titlePrincipals if principals.tconst === basic.tconst
      crew <- titleCrews if crew.tconst === basic.tconst
    } yield (basic, principals, crew)

    db.run(query.result).flatMap { results =>
      val groupedResults = results.groupBy(_._1)

      Future.sequence(groupedResults.map { case (titleBasic, group) =>
        val principalIds = group.map(_._2.nconst).distinct
        val crewIds = group.flatMap(itemGroup =>
          itemGroup._3.directors.getOrElse("").split(",") ++
          itemGroup._3.writers.getOrElse("").split(",")
        ).distinct

        for {
          principalNames <- getNamesByIds(principalIds)
          crewNames <- getNamesByIds(crewIds)
          principals = group.map(_._2).distinct.map { principal =>
            val nameBasic = principalNames.find(nameBasic => nameBasic.nconst == principal.nconst)
            PrincipalDTO(
              principal.nconst,
              nameBasic.flatMap(_.primaryName).getOrElse(""),
              nameBasic.flatMap(_.birthYear).getOrElse(0)
            )
          }
          crew = crewNames.collect {
            case nameBasic if crewIds.contains(nameBasic.nconst) =>
              val role =
                if (group.exists(_._3.directors.getOrElse("").split(",").contains(nameBasic.nconst))) "Director"
                else if (group.exists(_._3.writers.getOrElse("").split(",").contains(nameBasic.nconst))) "Writer"
                else ""
              CrewDTO(
                nameBasic.nconst,
                nameBasic.primaryName.getOrElse(""),
                nameBasic.birthYear.getOrElse(0),
                role
              )
          }
        } yield {
          MovieWithDetailsDTO(
            titleBasic.tconst,
            titleBasic.titleType.getOrElse(""),
            titleBasic.primaryTitle.getOrElse(""),
            titleBasic.originalTitle.getOrElse(""),
            crew,
            principals
          )
        }
      }.toSeq)
    }
  }

  def getNamesByIds(ids: Seq[String]): Future[Seq[NameBasic]] = handleExceptions {
    val query = nameBasics.filter(_.nconst inSet ids)
    db.run(query.result)
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
