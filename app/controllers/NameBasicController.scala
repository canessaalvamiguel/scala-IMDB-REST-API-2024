package controllers

import javax.inject._
import services.NameBasicService
import models.NameBasic
import play.api.libs.json._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class NameBasicController @Inject()(val controllerComponents: ControllerComponents, nameBasicService: NameBasicService)(implicit ec: ExecutionContext) extends BaseController {

  implicit val nameBasicFormat: OFormat[NameBasic] = Json.format[NameBasic]

  def list() = Action.async {
    nameBasicService.getAll().map { nameBasics =>
      Ok(Json.toJson(nameBasics))
    }
  }

  def create() = Action.async(parse.json) { request =>
    request.body.validate[NameBasic].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors)))),
      nameBasic => {
        nameBasicService.create(nameBasic).map { _ =>
          Created(Json.toJson(nameBasic))
        }
      }
    )
  }

  def read(nconst: String) = Action.async {
    nameBasicService.getById(nconst).map {
      case Some(nameBasic) => Ok(Json.toJson(nameBasic))
      case None => NotFound(Json.obj("message" -> s"NameBasic with nconst $nconst not found"))
    }
  }

  def update(nconst: String) = Action.async(parse.json) { request =>
    request.body.validate[NameBasic].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors)))),
      updatedNameBasic => {
        nameBasicService.update(nconst, updatedNameBasic).map {
          case 0 => NotFound(Json.obj("message" -> s"NameBasic with nconst $nconst not found"))
          case _ => NoContent
        }
      }
    )
  }

  def delete(nconst: String) = Action.async {
    nameBasicService.delete(nconst).map {
      case 0 => NotFound(Json.obj("message" -> s"NameBasic with nconst $nconst not found"))
      case _ => NoContent
    }
  }
}
