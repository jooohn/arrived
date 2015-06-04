package domain.user
import domain.location._
import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape
import java.sql.Timestamp
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

trait UserLocationRepository {
  def resolve(uid: Int): Future[Option[UserLocation]]
  def store(userLocation: UserLocation): Future[Option[UserLocation]]
}

object UserLocationRepository {
  def build: UserLocationRepository =
    new UserLocationRepositoryImpl
}

private class UserLocationRepositoryImpl
  extends UserLocationRepository
{
  val db = Database.forConfig("postgres")
  val userLocations = TableQuery[UserLocationTable]
  // db.run(userLocations.schema.create).value

  def resolve(uid: Int): Future[Option[UserLocation]] = {
    val query = userLocations.filter(_.uid === uid).take(1)
    db.run(query.result) map { rows =>
      rows.headOption map {
        case (_, latitude, longitude, timestamp) =>
          val location = new Location(latitude, longitude)
          UserLocation.apply(uid, location, timestamp)
      }
    }
  }

  def store(userLocation: UserLocation):
  Future[Option[UserLocation]] = {
    val location = userLocation.location;
    val query = userLocations
      .insertOrUpdate((
        userLocation.uid,
        location.latitude,
        location.longitude,
        userLocation.updatedAt
      ))
    db.run(query) flatMap { _ =>
      resolve(userLocation.uid)
    }
  }
}

private class UserLocationTable(tag: Tag)
  extends Table[(Int, BigDecimal, BigDecimal, Timestamp)](tag, "user_location") {

  // This is the primary key column:
  def uid = column[Int]("uid", O.PrimaryKey)
  def latitude = column[BigDecimal]("latitude")
  def longitude = column[BigDecimal]("longitude")
  def updatedAt = column[Timestamp]("updated_at")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (uid, latitude, longitude, updatedAt)

  def ins = (uid, latitude, longitude, updatedAt)
}
