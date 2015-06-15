package controllers

import domain.event.EventRepository
import domain.user._
import domain.location._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
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
        val after = Await.result(repository.store(ul), 5 seconds)

        // hook
        val eventRepository = EventRepository.build
        eventRepository.findByUid(uid) map { events =>
          events.filter(_.fit(before.map(_.location), after.location)).foreach { event =>
            Future { event.invoke } onFailure {
              case t => Logger.error("failed to execute event. (%s)".format(t.getMessage))
            }
          }
        }

        val response = Json.obj(
          "before" -> (before match {
            case Some(b) => Json.toJson(b)
            case _ => JsNull
          }),
          "after" -> Json.toJson(after)
        )
        Ok(response)
      }
    )
  }
}
