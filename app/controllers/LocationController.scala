package controllers

import models._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

class LocationController extends Controller {

  implicit val locationWrites: Writes[Location] = (
    (JsPath \ "latitude").write[Double] and
      (JsPath \ "longitude").write[Double]
    )(unlift(Location.unapply))

  implicit val locationReads: Reads[Location] = (
    (JsPath \ "latitude").read[Double] and
      (JsPath \ "longitude").read[Double]
    )(Location.apply _)

  def list = Action {
    Ok(Json.toJson(Location.list))
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
