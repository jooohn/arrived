package serializer
import play.api.libs.json._
import play.api.libs.functional.syntax._
import domain.location._
import domain.user._
import domain.event._

object Serializers {
  implicit val locationWrites: Writes[Location] = (
    (JsPath \ "latitude").write[BigDecimal] and
      (JsPath \ "longitude").write[BigDecimal]
    )(unlift(Location.unapply))

  implicit val locationReads: Reads[Location] = (
    (JsPath \ "latitude").read[BigDecimal] and
      (JsPath \ "longitude").read[BigDecimal]
    )(Location.apply _)

  implicit val AreaWrites: Writes[Area] = (
      (JsPath \ "location").write[Location] and
      (JsPath \ "distance").write[BigDecimal]
    )(unlift(Area.unapply))

  implicit val AreaReads: Reads[Area] = (
    (JsPath \ "location").read[Location] and
      (JsPath \ "distance").read[BigDecimal]
    )(Area.apply _)

  implicit val userLocationWrites: Writes[UserLocation] = (
    (JsPath \ "uid").write[Int] and
    (JsPath \ "location").write[Location] and
    (JsPath \ "updatedAt").write[Long]
  )(unlift(unapplyUserLocation))

  private def unapplyUserLocation(ul: UserLocation) =
    Some((ul.uid, ul.location, ul.updatedAt.getTime))

  implicit val UserAreaWrites: Writes[UserArea] = (
    (JsPath \ "id").write[Option[Int]] and
      (JsPath \ "uid").write[Int] and
      (JsPath \ "name").write[String] and
      (JsPath \ "area").write[Area]
    )(unlift(unapplyUserArea))

  def buildUserAreaReads(uid: Int): Reads[UserArea] = {
    ((JsPath \ "name").read[String] and
      (JsPath \ "area").read[Area]
    )((name, area) => UserArea.apply(None, uid, name, area))
  }

  implicit val EventWrites: Writes[Event] = (
    (JsPath \ "id").write[Option[Int]] and
      (JsPath \ "userArea").write[UserArea] and
      (JsPath \ "triggerType").write[Int] and
      (JsPath \ "actionType").write[Int] and
      (JsPath \ "options").write[String]
    )(unlift((event: Event) => Some(event.id, event.userArea, event.triggerType.id, event.actionType.id, event.options)))

  def buildEventReads(userArea: UserArea): Reads[Event] = {
    ((JsPath \ "triggerType").read[Int] and
      (JsPath \ "actionType").read[Int] and
      (JsPath \ "options").read[String]
      )((triggerType, actionType, options) => {
      Event.apply(
        None,
        userArea,
        Event.TriggerType(triggerType),
        Event.ActionType(actionType),
        options
      )})
  }


  private def unapplyUserArea(ua: UserArea) = Some(ua.id, ua.uid, ua.name, ua.area)
}
