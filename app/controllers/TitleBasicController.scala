package controllers

import models.TitleBasic
import play.api.libs.json._
import play.api.mvc._
import services.TitleBasicService
import services.dto.MovieWithDetailsDTO

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TitleBasicController @Inject()(
                                      val controllerComponents: ControllerComponents,
                                      titleBasicService: TitleBasicService,
                                      customAction: CustomActionBuilder
                                    )
                                    (implicit ec: ExecutionContext) extends BaseController{

  implicit val titleBasicFormat: OFormat[TitleBasic] = Json.format[TitleBasic]
  implicit val movieDetailsFormat: OFormat[MovieWithDetailsDTO] = Json.format[MovieWithDetailsDTO]

  def list() = Action.async {
    titleBasicService.getAll().map { titleBasics =>
      Ok(Json.toJson(titleBasics))
    }
  }

  def create() = customAction.async(parse.json) { request =>
    request.body.validate[TitleBasic].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors)))),
      titleBasic => {
        titleBasicService.create(titleBasic).map { _ =>
          Created(Json.toJson(titleBasic))
        }
      }
    )
  }

  def read(tconst: String) = customAction.async {
    titleBasicService.getById(tconst).map {
      case Some(titleBasic) => Ok(Json.toJson(titleBasic))
      case None => NotFound(Json.obj("message" -> s"TitleBasic with tconst $tconst not found"))
    }
  }

  def update(tconst: String) = customAction.async(parse.json) { request =>
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

  def delete(tconst: String) = customAction.async {
    titleBasicService.delete(tconst).map {
      case 0 => NotFound(Json.obj("message" -> s"TitleBasic with tconst $tconst not found"))
      case _ => NoContent
    }
  }

  def searchByTitle(title: String) = customAction.async { implicit request =>
    if (title.length < 3) {
      val errorResponse = Json.obj(
        "error" -> "Title must be at least 3 characters long"
      )
      Future.successful(BadRequest(errorResponse))
    } else {
      titleBasicService.searchByTitle(title).map { movies =>
        Ok(Json.toJson(movies))
      }
    }
  }
}
