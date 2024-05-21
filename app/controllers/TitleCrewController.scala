package controllers

import javax.inject._
import services.TitleCrewService
import models.TitleCrew
import play.api.libs.json._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TitleCrewController @Inject()(val controllerComponents: ControllerComponents, titleCrewService: TitleCrewService)(implicit ec: ExecutionContext) extends BaseController {

  implicit val titleCrewFormat: OFormat[TitleCrew] = Json.format[TitleCrew]

  def list() = Action.async {
    titleCrewService.getAll().map { titleCrews =>
      Ok(Json.toJson(titleCrews))
    }
  }

  def create() = Action.async(parse.json) { request =>
    request.body.validate[TitleCrew].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors)))),
      titleCrew => {
        titleCrewService.create(titleCrew).map { _ =>
          Created(Json.toJson(titleCrew))
        }
      }
    )
  }

  def read(tconst: String) = Action.async {
    titleCrewService.getById(tconst).map {
      case Some(titleCrew) => Ok(Json.toJson(titleCrew))
      case None => NotFound(Json.obj("message" -> s"TitleCrew with tconst $tconst not found"))
    }
  }

  def update(tconst: String) = Action.async(parse.json) { request =>
    request.body.validate[TitleCrew].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors)))),
      updatedTitleCrew => {
        titleCrewService.update(tconst, updatedTitleCrew).map {
          case 0 => NotFound(Json.obj("message" -> s"TitleCrew with tconst $tconst not found"))
          case _ => NoContent
        }
      }
    )
  }

  def delete(tconst: String) = Action.async {
    titleCrewService.delete(tconst).map {
      case 0 => NotFound(Json.obj("message" -> s"TitleCrew with tconst $tconst not found"))
      case _ => NoContent
    }
  }
}
