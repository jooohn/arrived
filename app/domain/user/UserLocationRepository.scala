package domain.user
import domain.location._
import play.api.db.DB
import play.api.Play.current
import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape
import java.sql.Timestamp
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

trait UserLocationRepository {
  def resolve(uid: Int): Future[Option[UserLocation]]
  def store(userLocation: UserLocation): Future[UserLocation]
}

object UserLocationRepository {
  def build: UserLocationRepository =
    new UserLocationRepositoryImpl
}

private class UserLocationRepositoryImpl
  extends UserLocationRepository
{
  val db = Database.forDataSource(DB.getDataSource())

  def resolve(uid: Int): Future[Option[UserLocation]] = {
    db.run(UserLocations.find(uid).result) map {
      _.headOption map { row =>
        val location = new Location(row.latitude, row.longitude)
        UserLocation.apply(row.uid, location, row.updatedAt)
      }
    }
  }

  def store(userLocation: UserLocation):
  Future[UserLocation] = {
    db.run(UserLocations.save(userLocation)) map { _ =>
      userLocation
    }
  }
}

private case class UserLocationRow(
  uid: Int,
  latitude: BigDecimal,
  longitude: BigDecimal,
  updatedAt: Timestamp)

private class UserLocations(tag: Tag)
  extends Table[UserLocationRow](tag, "user_location") {

  // This is the primary key column:
  def uid = column[Int]("uid", O.PrimaryKey)
  def latitude = column[BigDecimal]("latitude")
  def longitude = column[BigDecimal]("longitude")
  def updatedAt = column[Timestamp]("updated_at")

  def * = (uid, latitude, longitude, updatedAt) <>
    (UserLocationRow.tupled, UserLocationRow.unapply)
}

private object UserLocations extends TableQuery(new UserLocations(_)) {
  def find(uid: Int) = filter(_.uid === uid).take(1)

  def save(ul: UserLocation) = {
    val row = UserLocationRow(
      ul.uid,
      ul.location.latitude,
      ul.location.longitude,
      ul.updatedAt
    )
    this.insertOrUpdate(row)
  }
}
