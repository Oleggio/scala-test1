package repository

import java.sql.Time
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

// Use H2Profile to connect to an H2 database
import slick.jdbc.H2Profile.api._

import domain.RoomBooking

object Repository {

  class RoomBookings(tag: Tag)
    extends Table[RoomBooking](tag, "ROOM_BOOKINGS") {
    def roomId = column[Long]("id")
    def bookingStart = column[Time]("booking_start")
    def bookingEnd = column[Time]("booking_end")
    def * =
      (roomId, bookingStart, bookingEnd) <> ((RoomBooking.apply _).tupled, RoomBooking.unapply)
  }

  val bookings = TableQuery[RoomBookings]

  // We use in memory mode for H2
  val db = Database.forConfig("h2mem")

  def InitiateRepository(): Unit = {
    val setup = DBIO.seq(bookings.schema.create)
    db.run(setup)
  }

  def save(roomBooking: RoomBooking): String = {
    if (isRoomAvailable(roomBooking.roomId,
      roomBooking.bookingStart,
      roomBooking.bookingEnd)) {
      db.run(bookings += roomBooking)
      return "Booking confirmed"
    } else return "Booking unavailable.Choose another room or time."
  }

  def isRoomAvailable(roomId: Long,
                      proposedStart: Time,
                      proposedEnd: Time): Boolean = {
    val futureLookup = db.run(
      bookings
        .filter(b =>
          b.roomId === roomId &&
            (b.bookingEnd > proposedStart && b.bookingStart < proposedEnd))
        .exists
        .result)

    return !Await.result(futureLookup,
      scala.concurrent.duration.Duration(10, "seconds"))

  }

  def lookUpAll(proposedStart: Time, proposedEnd: Time): Set[Long] = {
    // Assumption that we have 10 rooms on premise
    var res: Set[Long] = (1L to 10L).toSet

    // If an existing booking starts/started before or at the same time as the proposed start
    //   AND finishes after the the proposed start
    // OR if an existing booking starts after the proposed start
    //   AND finishes before or at the same time as the proposed end
    // OR if an existing booking starts after the proposed start
    //   BUT not later than the proposed end
    // Then exclude the room from the list
    val q = bookings.filter(b =>
      b.bookingEnd > proposedStart && b.bookingStart < proposedEnd)

    // Iteratively go through all the entries and exclude rooms that overlap with proposed booking time
    val futureLookup = db.stream(q.result).foreach { b =>
    {
      if (res.contains(b.roomId))
      {
        res -= b.roomId
      }
    }
    }

    Await.result(futureLookup, 5000 millis);

    return res
  }
}
