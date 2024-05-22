package controllers

import javax.inject._
import services.TitlePrincipalService
import models.TitlePrincipal
import play.api.libs.json._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TitlePrincipalController @Inject()(val controllerComponents: ControllerComponents,
                                         titlePrincipalService: TitlePrincipalService,
                                         customAction: CustomActionBuilder
                                        )
                                        (implicit ec: ExecutionContext) extends BaseController {

  implicit val titlePrincipalFormat: OFormat[TitlePrincipal] = Json.format[TitlePrincipal]

  def list() = customAction.async {
    titlePrincipalService.getAll().map { titlePrincipals =>
      Ok(Json.toJson(titlePrincipals))
    }
  }

  def create() = customAction.async(parse.json) { request =>
    request.body.validate[TitlePrincipal].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors)))),
      titlePrincipal => {
        titlePrincipalService.create(titlePrincipal).map { _ =>
          Created(Json.toJson(titlePrincipal))
        }
      }
    )
  }

  def read(tconst: String, ordering: Int, nconst: String) = customAction.async {
    titlePrincipalService.getById(tconst, ordering, nconst).map {
      case Some(titlePrincipal) => Ok(Json.toJson(titlePrincipal))
      case None => NotFound(Json.obj("message" -> s"TitlePrincipal with tconst $tconst, ordering $ordering, and nconst $nconst not found"))
    }
  }

  def update(tconst: String, ordering: Int, nconst: String) = customAction.async(parse.json) { request =>
    request.body.validate[TitlePrincipal].fold(
      errors => Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors)))),
      updatedTitlePrincipal => {
        titlePrincipalService.update(tconst, ordering, nconst, updatedTitlePrincipal).map {
          case 0 => NotFound(Json.obj("message" -> s"TitlePrincipal with tconst $tconst, ordering $ordering, and nconst $nconst not found"))
          case _ => NoContent
        }
      }
    )
  }

  def delete(tconst: String, ordering: Int, nconst: String) = customAction.async {
    titlePrincipalService.delete(tconst, ordering, nconst).map {
      case 0 => NotFound(Json.obj("message" -> s"TitlePrincipal with tconst $tconst, ordering $ordering, and nconst $nconst not found"))
      case _ => NoContent
    }
  }
}
