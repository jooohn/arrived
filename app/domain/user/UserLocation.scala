package domain.user
import domain.location._
import java.sql.Timestamp

trait UserLocation {
  val uid: Int
  val location: Location
  val updatedAt: Timestamp
}

private case class UserLocationImpl (
  uid: Int,
  location: Location,
  updatedAt: Timestamp
) extends UserLocation

object UserLocation {
  def apply(
    uid: Int,
    location: Location,
    updatedAt: Timestamp = new Timestamp(System.currentTimeMillis())
    ): UserLocation =
    new UserLocationImpl(uid, location, updatedAt)
}
