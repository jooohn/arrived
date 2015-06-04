package serializer.location
import play.api.libs.json._
import play.api.libs.functional.syntax._
import domain.location.Location

object LocationSerializer {
  implicit val locationWrites: Writes[Location] = (
    (JsPath \ "latitude").write[BigDecimal] and
      (JsPath \ "longitude").write[BigDecimal]
    )(unlift(Location.unapply))

  implicit val locationReads: Reads[Location] = (
    (JsPath \ "latitude").read[BigDecimal] and
      (JsPath \ "longitude").read[BigDecimal]
    )(Location.apply _)
}
