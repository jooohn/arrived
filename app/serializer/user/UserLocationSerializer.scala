package serializer.user
import play.api.libs.json._
import play.api.libs.functional.syntax._
import domain.user.UserLocation
import domain.location.Location
import serializer.location.LocationSerializer._

object UserLocationSerializer {
  implicit val userLocationWrites: Writes[UserLocation] = (
    (JsPath \ "uid").write[Int] and
    (JsPath \ "location").write[Location] and
    (JsPath \ "updatedAt").write[Long]
  )(unlift(unapply))

  private def unapply(ul: UserLocation) = {
    Some((ul.uid, ul.location, ul.updatedAt.getTime))
  }
}
