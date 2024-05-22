package controllers

import play.api.libs.json.Json
import play.api.mvc._
import services.exceptions.DatabaseException

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class CustomActionBuilder @Inject()(parser: BodyParsers.Default)(implicit ec: ExecutionContext) extends ActionBuilderImpl(parser) {
  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
    block(request).recover {
      case ex: DatabaseException =>
        Results.InternalServerError(Json.obj("error" -> ex.getMessage))
    }
  }
}