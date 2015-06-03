package controllers

import domain.user._
import domain.location._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

class LocationController extends Controller {

  implicit val locationWrites: Writes[Location] = (
    (JsPath \ "latitude").write[BigDecimal] and
      (JsPath \ "longitude").write[BigDecimal]
    )(unlift(Location.unapply))

  implicit val locationReads: Reads[Location] = (
    (JsPath \ "latitude").read[BigDecimal] and
      (JsPath \ "longitude").read[BigDecimal]
    )(Location.apply _)

  def list = Action {
    UserRepository.ofSlick.resolve(1).value match {
      case user: User => Ok(Json.toJson(user.location))
      case _ => Ok
    }
  }

  def update = Action(BodyParsers.parse.json) { request =>
    val locationResult = request.body.validate[Location]
    locationResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "NG"))
      },
      location => {
        Location.save(location)
        Ok(Json.obj("status" -> "OK"))
      }
    )
  }

}
