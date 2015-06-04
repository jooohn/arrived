package controllers

import domain.user._
import domain.location._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.concurrent._
import scala.concurrent.duration._
import serializer.location.LocationSerializer._
import serializer.user.UserLocationSerializer._

class LocationController extends Controller {

  // dummy value
  val uid = 1

  def list = Action {
    val future = UserLocationRepository.build.resolve(uid)
    Await.result(future, 5 seconds) match {
      case Some(userLocation) =>
        Ok(Json.toJson(userLocation))
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
        val repository = UserLocationRepository.build
        val before = Await.result(repository.resolve(uid), 5 seconds)
        val ul = UserLocation.apply(uid, location)
        Await.result(repository.store(ul), 5 seconds) match {
          case Some(userLocation) => {
            val beforeJson = before match {
              case Some(b) => Json.toJson(b)
              case _ => JsNull
            }
            val response = Json.obj(
              "before" -> beforeJson,
              "after" -> Json.toJson(userLocation)
            )
            Ok(response)
          }
          case _ => BadRequest
        }
      }
    )
  }

}
