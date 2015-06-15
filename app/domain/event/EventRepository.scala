package domain.event
import domain.user._
import play.api.db.DB
import play.api.Play.current
import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape
import java.sql.Timestamp
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

trait EventRepository {
  def create(event: Event): Future[Event]
  def findByUid(uid: Int): Future[Seq[Event]]
  def findByUserArea(userArea: UserArea): Future[Seq[Event]]
}

object EventRepository {
  def build: EventRepository = new EventRepositoryImpl
}

private class EventRepositoryImpl extends EventRepository {
  val db = Database.forDataSource(DB.getDataSource())

  def create(event: Event): Future[Event] = {
    db.run(Events.insert(event)) map { id =>
      Event.apply(
        Some(id),
        event.userArea,
        event.triggerType,
        event.actionType,
        event.options
      )
    }
  }

  def findByUserArea(userArea: UserArea): Future[Seq[Event]] = {
    db.run(Events.findByUserAreaId(userArea.id.get).result) map { eventRows =>
      eventRows map { row =>
        Event.apply(
          row.id,
          userArea,
          Event.TriggerType(row.triggerType),
          Event.ActionType(row.actionType),
          row.options
        )
      }
    }
  }

  def findByUid(uid: Int): Future[Seq[Event]] = {
    db.run(Events.findByUid(uid).result) map { eventRows =>
      val userAreaRepository = UserAreaRepository.build
      eventRows map { row =>
        // TODO join
        val userArea = Await.result(userAreaRepository.resolve(row.userAreaId).map(_.get), 5 seconds)
        Event.apply(
          row.id,
          userArea,
          Event.TriggerType(row.triggerType),
          Event.ActionType(row.actionType),
          row.options
        )
      }
    }
  }
}

case class EventRow(
  id: Option[Int],
  uid: Int,
  userAreaId: Int,
  triggerType: Int,
  actionType: Int,
  options: String)

class Events(tag: Tag)
  extends Table[EventRow](tag, "event") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def uid = column[Int]("uid")
  def userAreaId = column[Int]("user_area_id")
  def triggerType = column[Int]("trigger_type")
  def actionType = column[Int]("action_type")
  def options = column[String]("options")

  def * = (id.?, uid, userAreaId, triggerType, actionType, options) <>
    (EventRow.tupled, EventRow.unapply)
}

object Events extends TableQuery(new Events(_)) {
  def findByUid(uid: Int) = filter(_.uid === uid)
  def findByUserAreaId(userAreaId: Int) = filter(_.userAreaId === userAreaId)

  def insert(event: Event) = {
    val row = EventRow(
      event.id,
      event.userArea.uid,
      event.userArea.id.get,
      event.triggerType.id,
      event.actionType.id,
      event.options
    )
    this returning this.map(_.id) += row
  }
}
