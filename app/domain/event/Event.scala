package domain.event

import domain.user._
import domain.location._

trait Event {
  val id: Option[Int]
  val userArea: UserArea
  val triggerType: Event.TriggerType
  val actionType: Event.ActionType
  val options: String

  def invoke: Unit
  def fit(previous: Option[Location], current: Location): Boolean
}

object Event {
  type TriggerType = TriggerType.Value
  type ActionType = ActionType.Value

  def apply(
    id: Option[Int],
    userArea: UserArea,
    triggerType: Event.TriggerType,
    actionType: Event.ActionType,
    options: String
    ): Event = EventImpl(id, userArea, triggerType, actionType, options)

  def decideTrigger(includedPrevious: Boolean, includedCurrent: Boolean) = {
    (includedPrevious, includedCurrent) match {
      case (true, true) => TriggerType.Staying
      case (true, false) => TriggerType.Left
      case (false, true) => TriggerType.Entered
      case (false, false) => TriggerType.Out
    }
  }

  object TriggerType extends Enumeration {
    val Entered = Value(1)
    val Staying = Value(2)
    val Left = Value(3)
    val Out = Value(4)
  }

  object ActionType extends Enumeration {
    val Log = Value(1)
    val Slack = Value(2)
    val Mail = Value(3)
    val Twitter = Value(4)
    val Voice = Value(5)
  }
}

private case class EventImpl(
  id: Option[Int],
  userArea: UserArea,
  triggerType: Event.TriggerType,
  actionType: Event.ActionType,
  options: String) extends Event {

  val action: EventAction = actionType match {
    case Event.ActionType.Log => new LogEventAction(this)
    case Event.ActionType.Slack => new SlackEventAction(this)
    case Event.ActionType.Mail => new MailEventAction(this)
    case Event.ActionType.Twitter => new TwitterEventAction(this)
    case Event.ActionType.Voice => new VoiceEventAction(this)
  }

  def fit(previous: Option[Location], current: Location) = {
    val currentTriggerType = Event.decideTrigger(
      userArea.include(previous),
      userArea.include(current)
    )
    triggerType == currentTriggerType
  }

  def invoke = this.action.invoke
}
