package domain.user
import domain.location._
import play.api.db.DB
import play.api.Play.current
import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape
import java.sql.Timestamp
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

trait UserAreaRepository {
  def resolve(id: Int): Future[Option[UserArea]]
  def list(uid: Int): Future[Seq[UserArea]]
  def create(userArea: UserArea): Future[UserArea]
}

object UserAreaRepository {
  def build: UserAreaRepository =
    new UserAreaRepositoryImpl
}

private class UserAreaRepositoryImpl
  extends UserAreaRepository
{
  val db = Database.forDataSource(DB.getDataSource())
  val userAreas = TableQuery[UserAreas]
  // db.run(userAreas.schema.create).value

  def resolve(id: Int): Future[Option[UserArea]] = {
    db.run(UserAreas.find(id).result) map {
      _.headOption map { row =>
        val location = Location.apply(row.latitude, row.longitude)
        val area = Area.apply(location, row.distance)
        UserArea.apply(Some(id), row.uid, row.name, area)
      }
    }
  }

  def list(uid: Int): Future[Seq[UserArea]] = {
    db.run(UserAreas.findByUid(uid).result) map {
      _ map { row =>
        val location = Location.apply(row.latitude, row.longitude)
        val area = Area.apply(location, row.distance)
        UserArea.apply(row.id, row.uid, row.name, area)
      }
    }
  }

  def create(userArea: UserArea): Future[UserArea] = {
    db.run(UserAreas.insert(userArea)) map { id =>
      UserArea.apply(Some(id), userArea.uid, userArea.name, userArea.area)
    }
  }
}

case class UserAreaRow(
  id: Option[Int],
  uid: Int,
  name: String,
  latitude: BigDecimal,
  longitude: BigDecimal,
  distance: BigDecimal)

class UserAreas(tag: Tag)
  extends Table[UserAreaRow](tag, "user_area") {

  // This is the primary key column:
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def uid = column[Int]("uid")
  def name = column[String]("name")
  def latitude = column[BigDecimal]("latitude")
  def longitude = column[BigDecimal]("longitude")
  def distance = column[BigDecimal]("distance")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, uid, name, latitude, longitude, distance) <>
    (UserAreaRow.tupled, UserAreaRow.unapply)
}

object UserAreas extends TableQuery(new UserAreas(_)) {
  def find(id: Int) = filter(_.id === id).take(1)
  // val findByUid = this.findBy(_.uid)
  def findByUid(uid: Int) = {
    this.filter(_.uid === uid)
  }
  def insert(userArea: UserArea) = {
    val row = UserAreaRow(
      userArea.id,
      userArea.uid,
      userArea.name,
      userArea.area.location.latitude,
      userArea.area.location.longitude,
      userArea.area.distance
    )
    this returning this.map(_.id) += row
  }

  def update(userArea: UserArea) = {
    val row = UserAreaRow(
      userArea.id,
      userArea.uid,
      userArea.name,
      userArea.area.location.latitude,
      userArea.area.location.longitude,
      userArea.area.distance
    )
    filter(_.id === userArea.id).update(row)
  }
}
