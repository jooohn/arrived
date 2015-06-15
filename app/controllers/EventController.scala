package controllers

import domain.user._
import domain.event._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.concurrent._
import scala.concurrent.duration._
import serializer.Serializers._
import scala.concurrent.ExecutionContext.Implicits.global

class EventController extends Controller {
  val uid = 1

  def list(areaId: Int) = Action.async {
    val userArea = UserAreaRepository.build.resolve(areaId)
    userArea flatMap {
      case Some(validArea) => {
        EventRepository.build.findByUserArea(validArea) map { events =>
          Ok(Json.toJson(events))
        }
      }
      case None => Future { BadRequest(Json.obj("status" -> "NG", "message" -> "invalid areaId")) }
    }
  }

  def create(areaId: Int) = Action.async(BodyParsers.parse.json) { request =>
    val userArea = UserAreaRepository.build.resolve(areaId)
    userArea flatMap {
      case Some(validArea) => {
        implicit val eventReads = buildEventReads(validArea)
        val eventResult = request.body.validate[Event]
        eventResult.fold(
          errors => {
            Future { BadRequest(Json.obj("status" -> "NG")) }
          },
          event => {
            EventRepository.build.create(event).map { created =>
              Ok(Json.toJson(created))
            }
          }
        )
      }
      case None => {
        Future { BadRequest(Json.obj("status" -> "NG", "message" -> "invalid areaId")) }
      }
    }
  }
}
