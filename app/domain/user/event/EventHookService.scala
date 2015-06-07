package domain.user.event

import domain.user._
import domain.location._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

trait EventHookService {

  def execute(
    hooks: Set[EventHook],
    previous: Option[Location],
    current: Location): Unit
}

object EventHookService {
  def apply: EventHookService = new EventHookServiceImpl
}

private class EventHookServiceImpl extends EventHookService {

  def execute(
    hooks: Set[EventHook],
    previous: Option[Location],
    current: Location) = {

    hooks.filter(_.fit(previous, current)).foreach { hook =>
      hook.actions.foreach { action =>
        Future { action.execute(hook) }
      }
    }
  }
}
