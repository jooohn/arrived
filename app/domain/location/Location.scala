package domain.location

case class Location(latitude: BigDecimal, longitude: BigDecimal)

object Location {
  var list: List[Location] = Nil

  def save(location: Location) = {
    list = location :: list
  }
}
