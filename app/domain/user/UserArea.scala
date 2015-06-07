package domain.user
import domain.location._
import scala.math.pow
import play.Logger

trait UserArea {
  val id: Option[Int]
  val uid: Int
  val name: String
  val area: Area

  def include(target: Location): Boolean
  def include(target: Option[Location]): Boolean = {
    target match {
      case Some(location) => include(location)
      case _ => false
    }
  }
}

private case class UserAreaImpl (
  id: Option[Int],
  uid: Int,
  name: String,
  area: Area
) extends UserArea {

  def include(target: Location) = {
    val diff =
      pow(area.location.latitude.toDouble - target.latitude.toDouble, 2) +
      pow(area.location.longitude.toDouble - target.longitude.toDouble, 2)
    pow(area.distance.toDouble, 2) >= diff
  }
}

object UserArea {
  def apply(id: Option[Int], uid: Int, name: String, area: Area): UserArea =
    new UserAreaImpl(id, uid, name, area)
}
