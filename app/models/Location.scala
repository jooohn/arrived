package models

case class Location(latitude: Double, longitude: Double)

object Location {
  var list: List[Location] = Nil

  def save(location: Location) = {
    list = location :: list
  }
}
