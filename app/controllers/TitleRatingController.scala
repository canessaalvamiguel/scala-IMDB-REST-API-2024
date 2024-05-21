package controllers

import javax.inject._
import services.TitleRatingService
import models.TitleRating
import play.api.libs.json._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TitleRatingController @Inject()(val controllerComponents: ControllerComponents, titleRatingService: TitleRatingService)(implicit ec: ExecutionContext) extends BaseController {

  implicit val titleRatingFormat: OFormat[TitleRating] = Json.format[TitleRating]

  def list() = Action.async {
    titleRatingService.getAll().map { titleRatings =>
      Ok(Json.toJson(titleRatings))
    }
  }

  def create() = Action.async(parse.json) { request =>
    request.body.validate[TitleRating].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors)))),
      titleRating => {
        titleRatingService.create(titleRating).map { _ =>
          Created(Json.toJson(titleRating))
        }
      }
    )
  }

  def read(tconst: String) = Action.async {
    titleRatingService.getById(tconst).map {
      case Some(titleRating) => Ok(Json.toJson(titleRating))
      case None => NotFound(Json.obj("message" -> s"TitleRating with tconst $tconst not found"))
    }
  }

  def update(tconst: String) = Action.async(parse.json) { request =>
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

  def delete(tconst: String) = Action.async {
    titleRatingService.delete(tconst).map {
      case 0 => NotFound(Json.obj("message" -> s"TitleRating with tconst $tconst not found"))
      case _ => NoContent
    }
  }
}
