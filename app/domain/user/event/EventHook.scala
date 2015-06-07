package domain.user.event

import domain.user._
import domain.location._

case class EventHook(
  id: Option[Int],
  uid: Int,
  userArea: UserArea,
  hookType: EventHook.HookType,
  actions: Set[EventAction]) {

  def fit(previous: Option[Location], current: Location) = {
    val currentHookType = EventHook.decideFookType(
      userArea.include(previous),
      userArea.include(current)
    )
    hookType == currentHookType
  }

}

object EventHook {
  type HookType = HookType.Value

  def decideFookType(includedPrevious: Boolean, includedCurrent: Boolean) = {
    (includedPrevious, includedCurrent) match {
      case (true, true) => HookType.Staying
      case (true, false) => HookType.Left
      case (false, true) => HookType.Entered
      case (false, false) => HookType.Out
    }
  }

  object HookType extends Enumeration {
    val Entered = Value(1)
    val Staying = Value(2)
    val Left = Value(3)
    val Out = Value(4)
  }
}
