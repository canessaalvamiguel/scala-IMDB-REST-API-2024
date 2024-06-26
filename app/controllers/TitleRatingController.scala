package controllers

import models.TitleRating
import play.api.libs.json._
import play.api.mvc._
import services.TitleRatingService

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TitleRatingController @Inject()(val controllerComponents: ControllerComponents,
                                      titleRatingService: TitleRatingService,
                                      customAction: CustomActionBuilder
                                     )
                                     (implicit ec: ExecutionContext) extends BaseController {

  implicit val titleRatingFormat: OFormat[TitleRating] = Json.format[TitleRating]

  def list() = customAction.async {
    titleRatingService.getAll().map { titleRatings =>
      Ok(Json.toJson(titleRatings))
    }
  }

  def create() = customAction.async(parse.json) { request =>
    request.body.validate[TitleRating].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors)))),
      titleRating => {
        titleRatingService.create(titleRating).map { _ =>
          Created(Json.toJson(titleRating))
        }
      }
    )
  }

  def read(tconst: String) = customAction.async {
    titleRatingService.getById(tconst).map {
      case Some(titleRating) => Ok(Json.toJson(titleRating))
      case None => NotFound(Json.obj("message" -> s"TitleRating with tconst $tconst not found"))
    }
  }

  def update(tconst: String) = customAction.async(parse.json) { request =>
    request.body.validate[TitleRating].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors)))),
      updatedTitleRating => {
        titleRatingService.update(tconst, updatedTitleRating).map {
          case 0 => NotFound(Json.obj("message" -> s"TitleRating with tconst $tconst not found"))
          case _ => NoContent
        }
      }
    )
  }

  def delete(tconst: String) = customAction.async {
    titleRatingService.delete(tconst).map {
      case 0 => NotFound(Json.obj("message" -> s"TitleRating with tconst $tconst not found"))
      case _ => NoContent
    }
  }

  def getTopRatedMoviesByGenre(genre: String) = customAction.async{
    if (genre.length < 3) {
      val errorResponse = Json.obj(
        "error" -> "Genre must be at least 3 characters long"
      )
      Future.successful(BadRequest(errorResponse))
    }else {
      titleRatingService.getTopRatedMoviesByGenre(genre).map { titleRatings =>
        Ok(Json.toJson(titleRatings))
      }
    }
  }
}
