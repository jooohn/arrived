package domain.event

abstract class EventAction(event: Event) {
  validate(event)

  def validate(event: Event): Unit
  def invoke: Unit
}
