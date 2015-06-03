package domain.user
import domain.location._

trait User {
  val uid: Integer
  var location: Location
}

private class UserImpl (
  val uid: Integer,
  var location: Location) extends User {
}

object User {
  def apply(uid: Integer, location: Location): User =
    new UserImpl(uid, location)
}
