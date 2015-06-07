package domain.user.event

import play.Logger
import scalaj.http._
import EventHook.HookType._

trait EventAction {
  val id: Option[Int]

  def execute(eventHook: EventHook): Unit
}

object EventAction {
  type ActionType = ActionType.Value

  def apply(
    id: Option[Int],
    actionType: ActionType): EventAction = {
    actionType match {
      case ActionType.Log => LogAction(id)
      case ActionType.Mail => MailAction(id)
    }
  }

  object ActionType extends Enumeration {
    val Log = Value(1)
    val Mail = Value(2)
  }
}

private case class LogAction(id: Option[Int])  extends EventAction {

  override def execute(eventHook: EventHook) = {
    val typeMessage = eventHook.hookType match {
      case Entered => "に入りました"
      case Staying => "に留まっています"
      case Left => "から離れました"
      case Out => "から離れています"
    }
    Logger.info("[%s] %s".format(eventHook.userArea.name, typeMessage))
  }
}

private case class MailAction(id: Option[Int]) extends EventAction {

  val endpoint = "http://salty-chamber-2415.herokuapp.com/api/performances"
  // val endpoint = "http://localhost:9000/location"

  override def execute(eventHook: EventHook) = {
    Logger.info("try to send mail via API (%s)".format(endpoint))
    val request = Http(endpoint)
    Logger.info("response: %s".format(request.asString))
  }
}
