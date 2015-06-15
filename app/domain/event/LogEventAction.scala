package domain.event
import play.Logger
import Event.TriggerType._

protected class LogEventAction(event: Event) extends EventAction(event) {
  val triggerType = event.triggerType
  val userArea = event.userArea

  def validate(event: Event) = Unit
  def invoke = {
    val typeMessage = triggerType match {
      case Entered => "に入りました"
      case Staying => "に留まっています"
      case Left => "から離れました"
      case Out => "から離れています"
    }
    Logger.info("[%s] %s".format(userArea.name, typeMessage))
  }
}
