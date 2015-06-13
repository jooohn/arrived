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

class ApplicationController extends Controller {

  // dummy value
  val uid = 1

  def index = Action {
    Ok(views.html.index())
  }
}
