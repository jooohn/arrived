package domain.user
import domain.location._
import slick.driver.PostgresDriver.api._
import slick.lifted.{ProvenShape}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

trait UserRepository {
  def resolve(uid: Int): Future[Option[User]]
}

object UserRepository {
  def ofSlick: UserRepository = new UserRepositoryOfSlick
}

private class UserRepositoryOfSlick extends UserRepository {
  val db = Database.forConfig("postgres")
  val userLocations = TableQuery[UserLocationTable]
  userLocations.schema.create

  def resolve(uid: Int): Future[Option[User]] = {
    val query = userLocations.filter(_.uid === uid).take(1)
    db.run(query.result) map {
      rows => rows.head
    } map {
      case (_, latitude, longitude) =>
        val location = new Location(latitude, longitude)
        val user = User.apply(uid, location)
        Some(user)
    }
  }
}

class UserLocationTable(tag: Tag)
  extends Table[(Int, BigDecimal, BigDecimal)](tag, "user_location") {

  // This is the primary key column:
  def uid = column[Int]("uid", O.PrimaryKey)
  def latitude = column[BigDecimal]("latitude")
  def longitude = column[BigDecimal]("longitude")

  // Every table needs a * projection with the same type as the table's type parameter
  def * : ProvenShape[(Int, BigDecimal, BigDecimal)] =
    (uid, latitude, longitude)
}
