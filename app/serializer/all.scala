package serializer
import play.api.libs.json._
import play.api.libs.functional.syntax._
import domain.location._
import domain.user._

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
    (JsPath \ "uid").write[Int] and
      (JsPath \ "name").write[String] and
      (JsPath \ "area").write[Area]
    )(unlift(unapplyUserArea))

  private def unapplyUserArea(ua: UserArea) = Some(ua.uid, ua.name, ua.area)
}
