package controllers

import javax.inject._
import services.TitleBasicService
import models.TitleBasic
import play.api.libs.json._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TitleBasicController @Inject()(val controllerComponents: ControllerComponents, titleBasicService: TitleBasicService)(implicit ec: ExecutionContext) extends BaseController {

  implicit val titleBasicFormat: OFormat[TitleBasic] = Json.format[TitleBasic]

  def list() = Action.async {
    titleBasicService.getAll().map { titleBasics =>
      Ok(Json.toJson(titleBasics))
    }
  }

  def create() = Action.async(parse.json) { request =>
    request.body.validate[TitleBasic].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors)))),
      titleBasic => {
        titleBasicService.create(titleBasic).map { _ =>
          Created(Json.toJson(titleBasic))
        }
      }
    )
  }

  def read(tconst: String) = Action.async {
    titleBasicService.getById(tconst).map {
      case Some(titleBasic) => Ok(Json.toJson(titleBasic))
      case None => NotFound(Json.obj("message" -> s"TitleBasic with tconst $tconst not found"))
    }
  }

  def update(tconst: String) = Action.async(parse.json) { request =>
    request.body.validate[TitleBasic].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors)))),
      updatedTitleBasic => {
        titleBasicService.update(tconst, updatedTitleBasic).map {
          case 0 => NotFound(Json.obj("message" -> s"TitleBasic with tconst $tconst not found"))
          case _ => NoContent
        }
      }
    )
  }

  def delete(tconst: String) = Action.async {
    titleBasicService.delete(tconst).map {
      case 0 => NotFound(Json.obj("message" -> s"TitleBasic with tconst $tconst not found"))
      case _ => NoContent
    }
  }
}
