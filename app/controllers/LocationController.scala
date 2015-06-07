package controllers

import domain.user._
import domain.user.event._
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
        val after = Await.result(repository.store(ul), 5 seconds)

        // hook
        val eventHooks = dummyEventHooks(uid)
        EventHookService.apply.execute(
          eventHooks,
          before.map(_.location),
          after.location
        )

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

  def dummyEventHooks(uid: Int): Set[EventHook] = {
    val area = Area.apply(Location.apply(0.0, 0.0), 10.0)
    val userArea = UserArea.apply(None, uid, "溜池山王", area)
    val logAction = EventAction.apply(None, EventAction.ActionType.Log)
    val mailAction = EventAction.apply(None, EventAction.ActionType.Mail)
    EventHook.HookType.values.map { hookType =>
      val actions = hookType match {
        case EventHook.HookType.Entered => Set(logAction, mailAction)
        case _ => Set(logAction)
      }
      EventHook.apply(None, uid, userArea, hookType, actions)
    }
  }
}
