package domain.event
import scalaj.http._
import play.Logger
import play.api.libs.json._
import Event.TriggerType._

protected class SlackEventAction(event: Event) extends EventAction(event) {
  val triggerType = event.triggerType
  val userArea = event.userArea
  val options = event.options
  val endpoint = "http://salty-chamber-2415.herokuapp.com/api/performances"

  def validate(event: Event) = Unit
  def invoke = {
    Logger.info("try to send message to slack (%s)".format(endpoint))
    val request = Http(endpoint).param("message", options)
    //   .postData(Json.obj("message" -> options).toString())
    //   .header("content-type", "application/json")
    Logger.info("response: %s".format(request.asString))
  }
}