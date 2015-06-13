package controllers

import domain.user._
import domain.location._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.concurrent._
import scala.concurrent.duration._
import serializer.Serializers._
import scala.concurrent.ExecutionContext.Implicits.global

class AreaController extends Controller {

  // dummy value
  val uid = 1

  def list = Action.async {
    val future = UserAreaRepository.build.list(uid)
    future.map { areas => Ok(Json.toJson(areas)) }
  }

  def create = Action { request =>
    BadRequest("under construction")
  }

  def update = Action(BodyParsers.parse.json) { request =>
    BadRequest("under construction")
  }

}
